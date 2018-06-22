package com.landlords.famgy.famgylandlords;

import android.os.Handler;

import com.landlords.famgy.famgylandlords.util.BeatHandler;

import java.util.Random;

public class Game {
    private static final Game game = new Game();

    Status status;
    Player[] players; //真人玩家编号为0
    int maxScore = 0; //当前叫地主分数最高的玩家分数
    int callBegin; //当前访问的下标
    Card cardHeap; //发牌堆

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
    }

    static Game getGame() {
        return game;
    }

    void startGame ()
    {
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

        //对应玩家加入地主牌
        int[] landlordCard = cardHeap.setLandlord (0);
        players[0].setHandCard ();

        status = Status.SetLandlord;

        BeatHandler.sendMessage(GameView.handlerV, "Set landlord");
    }

    void setLandlord() {
        BeatHandler.sendMessage(GameView.handlerV, "Set landlord ok");

        status = Status.Discard;
    }
}
