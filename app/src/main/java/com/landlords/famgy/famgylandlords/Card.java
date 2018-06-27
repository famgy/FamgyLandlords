package com.landlords.famgy.famgylandlords;
import java.util.Random;

class Card
{
	//一组牌的可能类型
	public enum Type
	{
		Unknown (0), //未知
		Single (1), //单张
		Double (2), //对子
		Three (3), //三条
		SingleSeq (4), //单顺
		DoubleSeq (5), //双顺
		ThreeSeq (6), //三顺
		ThreePlus (7), //三带一（一张或一对）
		Airplane (8), //飞机
		FourSeq (9), //四带二（两张或两对）
		Bomb (10); //炸弹、王炸

		private int value;

		Type (int value)
		{
			this.value = value;
		}

		int getValue ()
		{
			return value;
		}
	}

	private static Card card = new Card ();
	private int[] fullCard = new int[54];

	private Card()
	{
		for (int i = 0; i < 17; ++i)
			fullCard[i] = 0;
		for (int i = 17; i < 34; ++i)
			fullCard[i] = 1;
		for (int i = 34; i < 51; ++i)
			fullCard[i] = 2;
		for (int i = 51; i < 54; ++i)
			fullCard[i] = -1;
		swap ();
	}

	//交互手牌当作洗牌
	private void swap ()
	{
		Random random = new Random(System.currentTimeMillis ());
		int temp, a, b;
		for (int i = 0; i < 500; )
		{
			a = random.nextInt (54);
			b = random.nextInt (54);
			if (a != b)
			{
				temp = fullCard[a];
				fullCard[a] = fullCard[b];
				fullCard[b] = temp;
				++i;
			}
		}
	}

	//单例
	static Card getCard ()
	{
		return card;
	}

	//获取手牌
	boolean[] getHandCard (int No)
	{
		if (!(No > -1 && No < 3))
			throw new AssertionError();

		boolean[] handCard = new boolean[54];
		for (int i = 0; i < fullCard.length; ++i)
		{
			if (fullCard[i] == No) {
				handCard[i] = true;
			} else {
				handCard[i] = false;
			}
		}

		return handCard;
	}

	//获取手牌数量
	int getHandCardNum (int No)
	{
		if (!(No > -1 && No < 3))
			throw new AssertionError();
		int num = 0;
		for (int aFullCard : fullCard)
			if (aFullCard == No)
				++num;
		return num;
	}

	//分配地主牌
	int[] setLandlordCards (int No)
	{
		if (!(No > -1 && No < 3))
			throw new AssertionError();
		int[] landlordCard = new int[3];
		for (int i = 0, j = 0; i < fullCard.length; ++i)
			if (fullCard[i] == -1)
			{
				fullCard[i] = No;
				landlordCard[j] = i;
				++j;
			}

		//返回一个长度为三的数组，每个的值对应[54]中的一个下标，表示一张牌
		return landlordCard;
	}

	//打出去的牌
	void deleteCard (int i)
	{
		fullCard[i] = -1;
	}
}