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
    Player curPlayer; //当前出牌玩家
    Player lastPlayer; //最后出牌方
    Player landlord; //地主
    boolean bFitCards;

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
        bFitCards = true;
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

        curPlayer.passStatus = 3;

        Log.i("=== setLandlord ", "setLandlord send : " + "Discard select");
        BeatHandler.sendMessage(GameView.handlerV, "Discard select");
    }

    void discardSelectProc ()
    {
        Player player = game.players[0];

        if (player.passStatus == 3) {
            status = Status.DiscardSend;
        } else {
            analyseSelectCards();
            if (bFitCards == true) {
                status = Status.DiscardSend;
            } else {
                Log.i("=== discardSelectProc ", "select cards is wrong!");

                Log.i("=== discardSelectProc ", "setLandlord send : " + "select cards is wrong! Discard select again");
                BeatHandler.sendMessage(GameView.handlerV, "Discard select");

                return;
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
        }

        player.selectCards.clear();

        Log.i("=== discardSelectProc ", "discard send : " + "Discard send" + " , curStatus : " + status);
        BeatHandler.sendMessage(GameView.handlerV, "Discard send (discardSelectProc)");
    }

    //恢复出牌状态
    void discardSendProc ()
    {
        status = Status.Wait;
        Player oldPlayer = lastPlayer;

        if (curPlayer.myHandCards.isEmpty()) {
            status = Status.GameOver;
            Log.i("=== discardSendProc ", "discard send : " + "Discard gameOver" + " , curStatus : " + status);
            BeatHandler.sendMessage(GameView.handlerV, "Discard gameOver (discardSendProc)");
        }

        lastPlayer = curPlayer;
        if (lastPlayer == players[0]) {
            curPlayer = players[1];
        } else if (lastPlayer == players[1]) {
            curPlayer = players[2];
        } else if (lastPlayer == players[2]) {
            curPlayer = players[0];
            status = Status.DiscardSelect;
        }

        if (lastPlayer.passStatus == 3) {
            lastPlayer = oldPlayer;
        }

        if (lastPlayer == curPlayer && curPlayer == game.players[0]) {
            curPlayer.passStatus = 3;
        } else {
            curPlayer.passStatus = 1;
        }

        curPlayer.deskCards.clear();

        Log.i("=== discardSendProc ", "discard wait : " + "Discard wait" + " , curStatus : " + status);
        BeatHandler.sendMessage(GameView.handlerV, "Discard wait (discardSendProc)");
    }

    //等待状态转向下一位玩家出牌
    void discardwaitProc ()
    {
        Log.e("=== discardwaitProc ", "robotDiscard thread");

        if (curPlayer.getNo() == 0) {
            status = Status.DiscardSelect;

            Log.i("=== discardwaitProc ", "setLandlord send : " + "Discard select");
            BeatHandler.sendMessage(GameView.handlerV, "Discard select");
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                    robotDiscardSelectProc();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    //等待状态转向下一位玩家出牌
    void gameOverProc ()
    {
        Log.e("=== gameOverProc ", "game over!");
    }

    private void robotDiscardSelectProc()
    {
        DeskCard deskCard;
        int i = -1;
        status = Status.DiscardSend;

        /* 没人管， 走一张最小的牌 */
        if (curPlayer == lastPlayer) {
            deskCard = new DeskCard();
            deskCard.cardNo = curPlayer.myHandCards.get(curPlayer.myHandCards.size() - 1).cardNo;
            deskCard.type = Card.Type.Single;

            curPlayer.deskCards.add(deskCard);
            removeMyHandCardsNode(curPlayer.myHandCards, deskCard.cardNo);

            Log.i("=== robotDiscard ", "discard send : " + "Discard send");
            BeatHandler.sendMessage(GameView.handlerV, "Discard send" + " , curStatus : " + status);

            return;
        }

        /* 管牌 */
        ArrayList<MyHandCard> tmpMyHandCarsCpy = new ArrayList<>();
        tmpMyHandCarsCpy = (ArrayList<MyHandCard>) curPlayer.myHandCards.clone();

        if (lastPlayer.deskCards.get(0).type == Card.Type.Single)
        {
            for (i = tmpMyHandCarsCpy.size() - 1; i >= 0; i--) {
                if (tmpMyHandCarsCpy.get(i).cardNo / 4 > lastPlayer.deskCards.get(0).cardNo / 4) {
                    deskCard = new DeskCard();
                    deskCard.cardNo = tmpMyHandCarsCpy.get(i).cardNo;
                    deskCard.type = Card.Type.Single;

                    curPlayer.deskCards.add(deskCard);
                    removeMyHandCardsNode(curPlayer.myHandCards, deskCard.cardNo);

                    break;
                }
            }
        }
        else if (lastPlayer.deskCards.get(0).type == Card.Type.Double)
        {
            for (i = tmpMyHandCarsCpy.size() - 1; i >= 0; i--) {
                if (tmpMyHandCarsCpy.get(i).cardNo / 4 > lastPlayer.deskCards.get(0).cardNo / 4) {

                    if (i > 0 && tmpMyHandCarsCpy.get(i).cardNo / 4 == tmpMyHandCarsCpy.get(i + 1).cardNo / 4) {
                        deskCard = new DeskCard();
                        deskCard.cardNo = tmpMyHandCarsCpy.get(i).cardNo;
                        deskCard.type = Card.Type.Double;
                        curPlayer.deskCards.add(deskCard);
                        removeMyHandCardsNode(curPlayer.myHandCards, deskCard.cardNo);

                        deskCard = new DeskCard();
                        deskCard.cardNo = tmpMyHandCarsCpy.get(i + 1).cardNo;
                        curPlayer.deskCards.add(deskCard);
                        removeMyHandCardsNode(curPlayer.myHandCards, deskCard.cardNo);

                        break;
                    }
                }
            }
        }

        if (i < 0) {
            curPlayer.passStatus = 3;
        }

        Log.i("=== robotDiscard ", "discard send : " + "Discard send");
        BeatHandler.sendMessage(GameView.handlerV, "Discard send" + " , curStatus : " + status);
    }

    private void analyseSelectCards()
    {
        ArrayList<SelectCard> selectCards = curPlayer.selectCards;

        if (selectCards.size() == 0)
        {
            Log.i("Game", "Please select some cards");
            bFitCards = false;
        }
        else if (selectCards.size() == 1)
        {

            if (curPlayer.getNo() != lastPlayer.getNo() &&
                selectCards.get(0).cardNo / 4 <= lastPlayer.deskCards.get(0).cardNo / 4)
            {
                bFitCards = false;
                return;
            }

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

        return;
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
                Log.e("Game", "removeMyHandCardsNode start ...");

                myHandCards.remove(i);

                Log.e("Game", "removeMyHandCardsNode end ...");

                return;
            }
        }

        Log.e("GameView", "delListNode not remove one");
    }
}
