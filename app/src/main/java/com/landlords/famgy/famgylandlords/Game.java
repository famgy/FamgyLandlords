package com.landlords.famgy.famgylandlords;

import android.os.Handler;
import android.util.Log;

import com.landlords.famgy.famgylandlords.util.BeatHandler;

import java.util.ArrayList;
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
        DiscardSelect, //选牌
        DiscardSend, //出牌
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

    void startGameProc ()
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

        Log.i("=== handlerV ", "startGame send : " + "Game start ok");
        BeatHandler.sendMessage(GameView.handlerV, "Game start ok");
    }

    //叫地主
    void callLandlordProc () {
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
        int[] landlordCard = cardHeap.setLandlordCards (landlord.getNo());
        landlord.setHandCard ();
        if (landlord.getNo() == 0) {
            players[0].updateMyHandCardsInfo();
        }

        if (lord_show[0] == -1) {
            lord_show = Arrays.copyOf(landlordCard, landlordCard.length);
        }

        status = Status.SetLandlord;

        Log.i("=== callLandlord ", "callLandlord send : " + "Set landlord");
        BeatHandler.sendMessage(GameView.handlerV, "Set landlord");
    }

    void setLandlordProc() {
        status = Status.DiscardSelect;

        //如果是真人玩家
        if (curPlayer == players[0])
        {
            my_world = (curPlayer.getNo () == lastPlayer.getNo ());
        }

        Log.i("=== setLandlord ", "setLandlord send : " + "Discard select");
        BeatHandler.sendMessage(GameView.handlerV, "Discard select");
    }

    void discardSelectProc ()
    {
        boolean cards_fit = false;
        Player player = game.players[0];

        cards_fit = analyseSelectCards();
        if (cards_fit == true) {
            status = Status.DiscardSend;
        }

        for (int i = 0; i < player.selectCards.size(); i++)
        {
            SelectCard selectCard = player.selectCards.get(i);

            DeskCard deskCard = new DeskCard();
            deskCard.cardNo = selectCard.cardNo;

            if (i == 0) {
                deskCard.type = selectCard.type;
            }

            player.deskCards.add(deskCard);

            removeMyHandCardsNode(player.myHandCards, selectCard.cardNo);
        }

        player.selectCards.clear();

        Log.i("=== discardSelect ", "discard send : " + "Discard send");
        BeatHandler.sendMessage(GameView.handlerV, "Discard send" + " , curStatus : " + status);
    }

    //恢复出牌状态
    void discardSendProc ()
    {
        status = Status.Wait;

        lastPlayer = curPlayer;

        if (lastPlayer == players[0]) {
            curPlayer = players[1];
        } else if (lastPlayer == players[1]) {
            curPlayer = players[2];
        }

        Log.i("=== discardSend ", "discard wait : " + "Discard wait");
        BeatHandler.sendMessage(GameView.handlerV, "Discard wait" + " , curStatus : " + status);
    }

    //等待状态转向下一位玩家出牌
    void discardwaitProc ()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);

                    robotDiscard();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

//        Log.i("=== discardSend ", "discard wait : " + "Discard wait");
//        BeatHandler.sendMessage(GameView.handlerV, "Discard wait" + " , curStatus : " + status);
    }

    private void robotDiscard()
    {
        status = Status.DiscardSend;

        if (lastPlayer.deskCards.get(0).type == Card.Type.Single)
        {
            for (int i = curPlayer.myHandCards.size() - 1; i >= 0; i--) {
                if (curPlayer.myHandCards.get(i).cardNo / 4 > lastPlayer.deskCards.get(0).cardNo / 4) {
                    MyHandCard myHandCard = new MyHandCard();
                    myHandCard.cardNo = curPlayer.myHandCards.get(i).cardNo;

                    DeskCard deskCard = new DeskCard();
                    deskCard.cardNo = curPlayer.myHandCards.get(i).cardNo;
                    curPlayer.deskCards.add(deskCard);

                    break;
                }
            }
        }

        Log.i("=== robotDiscard ", "discard send : " + "Discard send");
        BeatHandler.sendMessage(GameView.handlerV, "Discard send" + " , curStatus : " + status);
    }

    private boolean analyseSelectCards()
    {
        boolean bFitCards = true;
        ArrayList<SelectCard> selectCards = curPlayer.selectCards;

        if (selectCards.size() == 0)
        {
            Log.i("Game", "Please select some cards");
            bFitCards = false;
        }
        else if (selectCards.size() == 1)
        {
            selectCards.get(0).type = Card.Type.Single;
        }
        else if (selectCards.size() == 2)
        {
            if (selectCards.get(0).cardNo > 52 && selectCards.get(1).cardNo > 52)
            {
                selectCards.get(0).type = Card.Type.Bomb;
            } else if (selectCards.get(0).cardNo / 4 == selectCards.get(1).cardNo / 4) {
                selectCards.get(0).type = Card.Type.Double;
            } else {
                bFitCards = false;
            }
        }

        return bFitCards;
    }

    public void sortAddList(ArrayList<SelectCard> selectCards, SelectCard selectCard) {
        SelectCard selectCardTmp;

        for (int i = 0; i < selectCards.size(); i++) {
            selectCardTmp = selectCards.get(i);
            if (selectCard.cardNo > selectCardTmp.cardNo) {
                selectCards.add(i, selectCard);
                return;
            }
        }

        selectCards.add(selectCard);
    }

    public void removeSelectCardsNode(ArrayList<SelectCard> selectCards, int No) {
        SelectCard selectCard = null;

        for (int i = 0; i < selectCards.size(); i++) {
            selectCard = selectCards.get(i);
            if (selectCard.cardNo == No) {
                selectCards.remove(i);
                return;
            }
        }

        Log.e("GameView", "delListNode not remove one");
    }

    public void removeMyHandCardsNode(ArrayList<MyHandCard> myHandCards, int No) {
        MyHandCard myHandCard = null;

        for (int i = 0; i < myHandCards.size(); i++) {
            myHandCard = myHandCards.get(i);
            if (myHandCard.cardNo == No) {
                synchronized (myHandCards) {
                    Log.e("Game", "removeMyHandCardsNode start ...");

                    myHandCards.remove(i);

                    Log.e("Game", "removeMyHandCardsNode end ...");
                }

                return;
            }
        }

        Log.e("GameView", "delListNode not remove one");
    }
}
