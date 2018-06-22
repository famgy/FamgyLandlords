package com.landlords.famgy.famgylandlords;

public class Player
{
    private int No;
    private Game game;
    private int callScore = 0;//叫地主的分数
    boolean[] handCard;//当前手牌，true表示有，false表示没有; 用0到53，从小到大排，0为方块三，53为大王

    Player (int No, Game game)
    {
        handCard = game.cardHeap.getHandCard (No);
        this.No = No;
        this.game = game;
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

    /*本局是否想当地主，并给出基本分
	因为在斗地主中，火箭、炸弹、王和2可以认为是大牌
	所以叫牌需要按照这些牌的多少来判断。下面是一个简单的原则：
	假定火箭为8分，炸弹为6分，大王4分，小王3分，一个2为2分，则当分数
	大于等于7分时叫三倍；
	大于等于5分时叫二倍；
	大于等于3分时叫一倍；
	小于三分不叫。*/
    //电脑玩家用
    int getBaseScoreAI ()
    {
        try
        {
            Thread.sleep (500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace ();
        }
        int sum = 0;
        //方便分析的权值-数量集合
        // 3-17权值集合，11->J，12->Q，13->K，14->1，15->2，16->小王，17->大王
        int[] group = new int[18];
        for (int i = 0; i < 54; ++i)
            if (handCard[i])
                ++group[CardGroup.translate (i)];

        if (group[17] == 1 && group[16] == 1) //如果有王炸
            sum += 8;

        else if (group[17] == 1) //如果有大王
            sum += 4;

        else if (group[16] == 1) //如果有小王
            sum += 3;

        //如果有2，每个2加2分
        sum += 2 * group[15];

        for (int i = 3; i < 16; ++i)
            if (group[i] == 4) //如果炸弹
                sum += 6;

        if (sum >= 7)
            return 3;

        else if (sum >= 5)
            return 2;

        else if (sum >= 3)
            return 1;

        else
            return 0;
    }
}
