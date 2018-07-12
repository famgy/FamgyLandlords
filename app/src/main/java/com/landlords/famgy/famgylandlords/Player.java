package com.landlords.famgy.famgylandlords;

import java.util.ArrayList;

public class Player
{
    private int No;
    private Game game;
    private int callScore = 0;//叫地主的分数
    boolean[] handCard;//当前手牌，true表示有，false表示没有; 用0到53，从小到大排，0为方块三，53为大王
    ArrayList<MyHandCard> myHandCards;
    ArrayList<SelectCard> selectCards;
    ArrayList<DeskCard> deskCards;
    int passStatus = 1; // 1：允许pass， 2：已选pass, 3 : 无权pass

    Player (int No, Game game)
    {
        handCard = game.cardHeap.getHandCard (No);
        this.No = No;
        this.game = game;

        myHandCards = new ArrayList<>();
        updateMyHandCardsInfoLandlord();

        selectCards = new ArrayList<>();
        deskCards = new ArrayList<>();
    }

    void updateMyHandCardsInfoLandlord() {
        int offset = 0;
        MyHandCard myHandCard;

        myHandCards.clear();

        for (int i = 53; i >= 0; i--)
        {
            if (handCard[i])
            {
                myHandCard = new MyHandCard();
                myHandCard.cardNo = i;
                myHandCard.retx = (100 + offset * 25) * GameActivity.SCREEN_WIDTH / 800;
                myHandCard.rety = 350 * GameActivity.SCREEN_HEIGHT / 480;
                myHandCard.rety_discard = 330 * GameActivity.SCREEN_HEIGHT / 480;
                myHandCard.bSelected = false;

                offset++;

                myHandCards.add(myHandCard);
            }
        }
    }

    void updateMyHandCardsShow() {

        MyHandCard myHandCard;
        int offset = 0;

        for (int i = 0; i < myHandCards.size(); i++) {
            myHandCard = myHandCards.get(i);
            myHandCard.retx = (100 + offset * 25) * GameActivity.SCREEN_WIDTH / 800;

            offset++;
        }
    }

    //获取编号
    int getNo ()
    {
        return No;
    }

    //更新手牌，用于加入地主牌
    void setHandCard ()
    {
        handCard = game.cardHeap.getHandCard (No);
    }

    int getCallScore ()
    {
        return callScore;
    }

    void setCallScore (int callScore)
    {
        this.callScore = callScore;
    }
}
