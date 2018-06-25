package com.landlords.famgy.famgylandlords;

import android.os.Handler;

import com.landlords.famgy.famgylandlords.util.BeatHandler;

import java.util.Arrays;
import java.util.Random;

public class Game {
    private static final Game game = new Game();

    Status status;
    Player[] players; //真人玩家编号为0
    int maxScore = 0; //当前叫地主分数最高的玩家分数
    int callBegin; //当前访问的下标
    Card cardHeap; //发牌堆
    int[] lord_show = new int[3];
    Boolean my_world = false;  //判断是否是自己任意出牌局；
    Player curPlayer; //当前出牌玩家
    Player lastPlayer; //最后出牌方
    Player landlord; //地主

    //游戏进度状态
    enum Status
    {
        NotStart, //游戏未开始
        GetLandlord, //叫地主阶段
        SetLandlord, //发地主牌阶段
        Discard, //出牌阶段
        Wait, //完成一局后等待选择
        GameOver //游戏结束
    }

    private Game() {
        status = Status.NotStart;

        lord_show[0] = -1;
    }

    static Game getGame() {
        return game;
    }

    void startGame ()
    {
        curPlayer = null;
        lastPlayer = null;
        //发牌函数
        cardHeap = Card.getCard (); //洗牌
        //初始化角色
        players = new Player[] {
                            new Player (0, this),
                            new Player (1, this),
                            new Player (2, this)};
        status = Status.GetLandlord;

        BeatHandler.sendMessage(GameView.handlerV, "Game start ok");
    }

    //叫地主
    void callLandlord () {
        //随机确定开始询问的玩家
        callBegin = new Random(System.currentTimeMillis ()).nextInt (3);

        //循环询问
        for (int questioned = 0; questioned < 3; ++questioned)
        {
            //四个按钮，不叫->0分，1分，2分，3分
            if (callBegin != 0) {
                /* 电脑玩家 */
                //players[                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     callBegin].setCallScore(players[callBegin].getBaseScoreAI());
            }
        }

        //地主先出牌
        landlord = players[0];
        curPlayer = landlord;
        lastPlayer = landlord;

        //对应玩家加入地主牌
        int[] landlordCard = cardHeap.setLandlord (landlord.getNo());
        landlord.setHandCard ();
        if (landlord.getNo() == 0) {
            players[0].updateMyHandCardsInfo();
        }

        if (lord_show[0] == -1) {
            lord_show = Arrays.copyOf(landlordCard, landlordCard.length);
        }

        status = Status.SetLandlord;

        BeatHandler.sendMessage(GameView.handlerV, "Set landlord");
    }

    void setLandlord() {
        status = Status.Discard;

        //如果是真人玩家
        if (curPlayer == players[0])
        {
            my_world = (curPlayer.getNo () == lastPlayer.getNo ());
        }

        BeatHandler.sendMessage(GameView.handlerV, "Discard");
    }

    //出牌
    void discard ()
    {
        BeatHandler.sendMessage(GameView.handlerV, "Discard send");
    }
}
