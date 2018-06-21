package com.landlords.famgy.famgylandlords;

import android.os.Handler;

import com.landlords.famgy.famgylandlords.util.BeatHandler;

public class Game {
    private static final Game game = new Game();
    private Handler handlerC;

    Status status;
    Player[] players; //真人玩家编号为0

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

    void startGame (Handler handler)
    {
        this.handlerC = handler;
        //发牌函数
        //cardHeap = Card.getCard (); //洗牌
        //初始化角色
        players = new Player[] {new Player (0, this),
                new Player (1, this), new Player (2, this)};
        status = Status.GetLandlord;

        BeatHandler.sendMessage(handlerC, "Get landlord");
    }

    //叫地主
    void callLandlord () {
        status = Status.SetLandlord;

        BeatHandler.sendMessage(handlerC, "Set landlord");
    }
}
