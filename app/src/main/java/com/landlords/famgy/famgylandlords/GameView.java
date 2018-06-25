package com.landlords.famgy.famgylandlords;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.landlords.famgy.famgylandlords.util.BeatHandler;
import com.landlords.famgy.famgylandlords.util.BitmapScala;
import com.landlords.famgy.famgylandlords.util.CardImage;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener{

    boolean flag = true;
    private SurfaceHolder surfaceHolder;
    private Resources resources;
    private CardImage cardImage;
    private Bitmap bitmap_background;
    private float background_w;
    private float background_h;
    private Bitmap bitmap_desk;
    private float desk_w;
    private float desk_h;
    private Bitmap bitmap_player1;
    private float player1_w;
    private float player1_h;
    private Bitmap bitmap_player2;
    private float player2_w;
    private float player2_h;
    private Bitmap bitmap_player3;
    private float player3_w;
    private float player3_h;
    private Bitmap bitmap_card_back;
    private float card_back_w;
    private float card_back_h;
    private float card_cur_w;
    private float card_cur_h;
    private Bitmap bitmap_no1;
    private float no1_w;
    private float no1_h;
    private Bitmap bitmap_score11;
    private Bitmap bitmap_score12;
    private Bitmap bitmap_score13;
    private Bitmap bitmap_score21;
    private Bitmap bitmap_score22;
    private Bitmap bitmap_score23;
    private Bitmap bitmap_score31;
    private Bitmap bitmap_score32;
    private Bitmap bitmap_score33;
    private float score_w;
    private float score_h;
    private Bitmap bitmaps_cards[];
    private Bitmap bitmaps_cards_l[];
    private Bitmap bitmaps_cards_d[];
    private float cards_d_w;
    private float cards_d_h;
    private Bitmap bitmap_discard;
    private float discard_w;
    private float discard_h;
    private Bitmap bitmap_pass;

    private Context context;

    public static Handler handlerV;
    private Game game;


    Runnable sendable = new Runnable ()
    {
        @Override
        public void run ()
        {
            Looper.prepare();

            handlerV = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String cmd = (String) msg.obj;
                    Log.i("=== HandlerV ", "received : " + cmd + " , curStatus :" + game.status);

                    draw();
                }
            };

            BeatHandler.sendMessage(GameActivity.handlerF, "VThread ok");

            Looper.loop();
        }
    };

    public GameView(Context context, Handler handler) {
        super(context);

        this.context = context;
        surfaceHolder = getHolder();

        initBitmaps();

        game = Game.getGame();

        surfaceHolder.addCallback(this);
        setOnTouchListener(this);
    }

    private void initBitmaps() {
        resources = getResources();
        background_w = MainActivity.SCREEN_WIDTH;
        background_h = MainActivity.SCREEN_HEIGHT;
        desk_w = 700 * GameActivity.SCREEN_WIDTH / 800;
        desk_h = 430 * GameActivity.SCREEN_HEIGHT / 480;
        player1_w = 88 * GameActivity.SCREEN_WIDTH / 800;
        player1_h = 142 * GameActivity.SCREEN_HEIGHT / 480;
        player2_w = 100 * GameActivity.SCREEN_WIDTH / 800;
        player2_h = 100 * GameActivity.SCREEN_HEIGHT / 480;
        player3_w = 128 * GameActivity.SCREEN_WIDTH / 800;
        player3_h = 142 * GameActivity.SCREEN_HEIGHT / 480;
        card_back_w = 40 * GameActivity.SCREEN_WIDTH / 800;
        card_back_h = 54 * GameActivity.SCREEN_HEIGHT / 480;
        card_cur_w = 80 * GameActivity.SCREEN_WIDTH / 800;
        card_cur_h = 120 * GameActivity.SCREEN_HEIGHT / 480;
        no1_w = 100 * GameActivity.SCREEN_WIDTH / 800;
        no1_h = 60 * GameActivity.SCREEN_HEIGHT / 480;
        score_w = 100 * GameActivity.SCREEN_WIDTH / 800;
        score_h = 60 * GameActivity.SCREEN_HEIGHT / 480;
        discard_w = 100 * GameActivity.SCREEN_WIDTH / 800;
        discard_h = 60 * GameActivity.SCREEN_HEIGHT / 480;
        cards_d_w = 40 * GameActivity.SCREEN_WIDTH / 800;
        cards_d_h = 60 * GameActivity.SCREEN_HEIGHT / 480;

        bitmap_background = BitmapScala.scalamap(BitmapFactory.decodeResource(resources, R.drawable.place), background_w, background_h);
        bitmap_desk = BitmapScala.scalamap(BitmapFactory.decodeResource (resources, R.drawable.desk), desk_w, desk_h);
        bitmap_player1 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.player1), player1_w, player1_h);
        bitmap_player2 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.player2), player2_w, player2_h);
        bitmap_player3 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.player3), player3_w, player3_h);
        bitmap_card_back = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.card_back), card_back_w, card_back_h);
        bitmap_no1 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.no1), no1_w, no1_h);
        bitmap_score11 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.score11), score_w, score_h);
        bitmap_score12 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.score12), score_w, score_h);
        bitmap_score13 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.score13), score_w, score_h);
        bitmap_score21 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.score21), score_w, score_h);
        bitmap_score22 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.score22), score_w, score_h);
        bitmap_score23 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.score23), score_w, score_h);
        bitmap_score31 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.score31), score_w, score_h);
        bitmap_score32 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.score32), score_w, score_h);
        bitmap_score33 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.score33), score_w, score_h);
        bitmap_discard = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.discard1), discard_w, discard_h);

        //初始化卡牌
        bitmaps_cards = new Bitmap[54];
        bitmaps_cards_l = new Bitmap[54];
        for (int i = 0; i < 54; i++)
        {
            Bitmap bitmap_card = BitmapFactory.decodeResource (resources, cardImage.cardImages[i / 4][i % 4]);
            bitmaps_cards[i] = BitmapScala.scalamap (bitmap_card, card_cur_w, card_cur_h);
            bitmaps_cards_l[i] = BitmapScala.scalamap (bitmap_card, card_back_w, card_back_h);
            bitmaps_cards_d[i] = BitmapScala.scalamap (bitmap_card, cards_d_w, cards_d_h);
            //game.select[i] = false;
        }
    }

    private void draw() {
        switch (game.status)
        {
            case GetLandlord:
                drawStart();
                break;
            case SetLandlord:
                drawLandlord();
                break;
            case Discard:
                drawDiscard();
                break;
            default:
                break;
        }
    }

    private void drawStart() {
        Rect src = new Rect();
        Rect des = new Rect();

        src.set(0, 0, bitmap_background.getWidth(), bitmap_background.getHeight());
        des.set(0, 0, GameActivity.SCREEN_WIDTH, GameActivity.SCREEN_HEIGHT);

        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            Paint paint = new Paint();

            canvas.drawBitmap(bitmap_background, src, des, paint);
            canvas.drawBitmap(bitmap_desk, 50 * GameActivity.SCREEN_WIDTH / 800, 50 * GameActivity.SCREEN_HEIGHT / 480, null);

            canvas.drawBitmap(bitmap_player1, 10 * GameActivity.SCREEN_WIDTH / 800, 326 * GameActivity.SCREEN_HEIGHT / 480, null);
            canvas.drawBitmap(bitmap_player2, 10 * GameActivity.SCREEN_WIDTH / 800, 25 * GameActivity.SCREEN_HEIGHT / 480, null);
            canvas.drawBitmap(bitmap_player3, 672 * GameActivity.SCREEN_WIDTH / 800, 5 * GameActivity.SCREEN_HEIGHT / 480, null);

            initPokers(canvas);
            draw_card_loard(canvas);
            draw_button_loard(canvas);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawLandlord() {
        Rect src = new Rect();
        Rect des = new Rect();

        src.set(0, 0, bitmap_background.getWidth(), bitmap_background.getHeight());
        des.set(0, 0, GameActivity.SCREEN_WIDTH, GameActivity.SCREEN_HEIGHT);

        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            Paint paint = new Paint();

            canvas.drawBitmap(bitmap_background, src, des, paint);
            canvas.drawBitmap(bitmap_desk, 50 * GameActivity.SCREEN_WIDTH / 800, 50 * GameActivity.SCREEN_HEIGHT / 480, null);

            canvas.drawBitmap(bitmap_player1, 10 * GameActivity.SCREEN_WIDTH / 800, 326 * GameActivity.SCREEN_HEIGHT / 480, null);
            canvas.drawBitmap(bitmap_player2, 10 * GameActivity.SCREEN_WIDTH / 800, 25 * GameActivity.SCREEN_HEIGHT / 480, null);
            canvas.drawBitmap(bitmap_player3, 672 * GameActivity.SCREEN_WIDTH / 800, 5 * GameActivity.SCREEN_HEIGHT / 480, null);

            initPokers(canvas);
            draw_card_loard(canvas);
            draw_button_loard(canvas);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }

        BeatHandler.sendMessage(GameActivity.handlerC, "Set landlord ok");
    }

    private void drawDiscard() {
        Rect src = new Rect();
        Rect des = new Rect();

        src.set(0, 0, bitmap_background.getWidth(), bitmap_background.getHeight());
        des.set(0, 0, GameActivity.SCREEN_WIDTH, GameActivity.SCREEN_HEIGHT);

        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            Paint paint = new Paint();

            canvas.drawBitmap(bitmap_background, src, des, paint);
            canvas.drawBitmap(bitmap_desk, 50 * GameActivity.SCREEN_WIDTH / 800, 50 * GameActivity.SCREEN_HEIGHT / 480, null);

            canvas.drawBitmap(bitmap_player1, 10 * GameActivity.SCREEN_WIDTH / 800, 326 * GameActivity.SCREEN_HEIGHT / 480, null);
            canvas.drawBitmap(bitmap_player2, 10 * GameActivity.SCREEN_WIDTH / 800, 25 * GameActivity.SCREEN_HEIGHT / 480, null);
            canvas.drawBitmap(bitmap_player3, 672 * GameActivity.SCREEN_WIDTH / 800, 5 * GameActivity.SCREEN_HEIGHT / 480, null);

            initPokers(canvas);
            draw_card_loard(canvas);

            if (game.select) {

            }
            draw_button_discard(canvas);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void initPokers(Canvas canvas) {
        ArrayList<MyHandCard> myHandCards = game.players[0].myHandCards;

        for (MyHandCard myHandCard : myHandCards) {
            canvas.drawBitmap (bitmaps_cards[myHandCard.cardNo], myHandCard.retx, myHandCard.rety, null);
        }
    }

    private void draw_card_loard(Canvas canvas) {
        if (game.status == Game.Status.NotStart || game.status == Game.Status.GetLandlord) {
            canvas.drawBitmap(bitmap_card_back, 320 * GameActivity.SCREEN_WIDTH / 800, 20 * GameActivity.SCREEN_HEIGHT / 480, null);
            canvas.drawBitmap(bitmap_card_back, 380 * GameActivity.SCREEN_WIDTH / 800, 20 * GameActivity.SCREEN_HEIGHT / 480, null);
            canvas.drawBitmap(bitmap_card_back, 440 * GameActivity.SCREEN_WIDTH / 800, 20 * GameActivity.SCREEN_HEIGHT / 480, null);
        } else {
            int[] a = game.lord_show;
            canvas.drawBitmap(bitmaps_cards_l[a[0]], 320 * GameActivity.SCREEN_WIDTH / 800, 20 * GameActivity.SCREEN_HEIGHT / 480, null);
            canvas.drawBitmap(bitmaps_cards_l[a[1]], 380 * GameActivity.SCREEN_WIDTH / 800, 20 * GameActivity.SCREEN_HEIGHT / 480, null);
            canvas.drawBitmap(bitmaps_cards_l[a[2]], 440 * GameActivity.SCREEN_WIDTH / 800, 20 * GameActivity.SCREEN_HEIGHT / 480, null);
        }


        canvas.drawBitmap(bitmap_card_back, 620 * GameActivity.SCREEN_WIDTH / 800, 100 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap(bitmap_card_back, 100 * GameActivity.SCREEN_WIDTH / 800, 100 * GameActivity.SCREEN_HEIGHT / 480, null);
    }

    private void draw_button_loard(Canvas canvas) {
        canvas.drawBitmap(bitmap_no1, 170 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);

        canvas.drawBitmap (bitmap_score11, 290 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap (bitmap_score21, 410 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap (bitmap_score31, 530 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);
    }

    private void draw_button_discard(Canvas canvas) {
        Resources resources = getResources ();
        if (game.my_world)
        {
            //   pass = BitmapScala.scalamap(BitmapFactory.decodeResource(resources, R.drawable.pass), 108* GameActivity.SCREEN_WIDTH / 800, 60 * GameActivity.SCREEN_HEIGHT / 480);
            bitmap_pass = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.pass3), discard_w, discard_h);
        }
        else
        {
            bitmap_pass = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.pass1), discard_w, discard_h);
        }

        canvas.drawBitmap (bitmap_pass, 290 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap (bitmap_discard, 410 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean bAction = false;
        float retx = motionEvent.getRawX();
        float rety = motionEvent.getRawY();

        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                bAction = playViewDown(retx, rety);
                break;
            case MotionEvent.ACTION_UP:
                bAction = playViewUp(retx, rety);
                break;
            default:
                bAction = false;
                break;
        }

        return bAction;
    }

    private boolean playViewDown(float retx, float rety) {
        boolean bAction = false;

        /* 叫3分 */
        if (retx > 530 * GameActivity.SCREEN_WIDTH / 800 &&
            retx < 530 * GameActivity.SCREEN_WIDTH / 800 + score_w &&
            rety > 260 * GameActivity.SCREEN_HEIGHT / 480 &&
            rety < 260 * GameActivity.SCREEN_HEIGHT / 480 + score_h)
        {
            bAction = true;
        }
        else if ((rety > 330 * GameActivity.SCREEN_HEIGHT / 480) && (rety < 330 * GameActivity.SCREEN_HEIGHT / 480 + card_cur_h) && (game.status == Game.Status.Discard))
        {
            if (clickHandCard(retx, rety) == true)
            {
                BeatHandler.sendMessage(handlerV, "Discard select");
            }

            bAction = false;
        }
        else if (retx > 410 * GameActivity.SCREEN_WIDTH / 800 && retx < 410 * GameActivity.SCREEN_WIDTH / 800 + discard_w
                && rety > 260 * GameActivity.SCREEN_HEIGHT / 480 && rety < 260 * GameActivity.SCREEN_HEIGHT / 480 + discard_h
                && game.status == Game.Status.Discard)
        {
            bitmap_discard = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.discard2), discard_w, discard_h);
            BeatHandler.sendMessage(handlerV, "Discard send down");

            bAction = true;
        }

        return bAction;
    }

    private boolean playViewUp(float retx, float rety) {
        boolean bAction = false;

        /* 叫3分 */
        if (retx > 530 * GameActivity.SCREEN_WIDTH / 800 &&
                retx < 530 * GameActivity.SCREEN_WIDTH / 800 + score_w &&
                rety > 260 * GameActivity.SCREEN_HEIGHT / 480 &&
                rety < 260 * GameActivity.SCREEN_HEIGHT / 480 + score_h)
        {
            bitmap_score31 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.score32), score_w, score_h);
            BeatHandler.sendMessage(GameActivity.handlerC, "Call landlord ok");

            bAction = true;
        }
        else if (retx > 410 * GameActivity.SCREEN_WIDTH / 800 && retx < 410 * GameActivity.SCREEN_WIDTH / 800 + discard_w
                && rety > 260 * GameActivity.SCREEN_HEIGHT / 480 && rety < 260 * GameActivity.SCREEN_HEIGHT / 480 + discard_h
                && game.status == Game.Status.Discard)
        {
            bitmap_discard = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.discard1), discard_w, discard_h);
            BeatHandler.sendMessage(handlerV, "Discard send up");
            BeatHandler.sendMessage(GameActivity.handlerC, "Discard send up");
        }
        else
        {
            bAction = false;
        }

        return bAction;
    }

    boolean clickHandCard(float retx, float rety) {
        ArrayList<MyHandCard> myHandCards = game.players[0].myHandCards;
        ArrayList<SelectCard> selectCards = game.players[0].selectCards;

        int i = 0;
        for (MyHandCard myHandCard : myHandCards) {
            if (i == myHandCards.size() - 1) {
                if (retx > myHandCard.retx && retx < myHandCard.retx + discard_w) {

                    if (myHandCard.bSelected == true) {
                        myHandCard.rety = 350 * GameActivity.SCREEN_HEIGHT / 480;
                        myHandCard.bSelected = false;

                        SelectCard selectCard = new SelectCard();
                        selectCard.cardNo = myHandCard.cardNo;
                        selectCards.remove(selectCard);
                    } else {
                        myHandCard.rety = 330 * GameActivity.SCREEN_HEIGHT / 480;
                        myHandCard.bSelected = true;

                        SelectCard selectCard = new SelectCard();
                        selectCard.cardNo = myHandCard.cardNo;
                        selectCards.add(selectCard);
                    }

                    return true;
                }
            }
            else
            {
               if (retx > myHandCard.retx && retx < myHandCard.retx + 25 * GameActivity.SCREEN_WIDTH / 800) {

                   if (myHandCard.bSelected == true) {
                       myHandCard.rety = 350 * GameActivity.SCREEN_HEIGHT / 480;
                       myHandCard.bSelected = false;
                   } else {
                       myHandCard.rety = 330 * GameActivity.SCREEN_HEIGHT / 480;
                       myHandCard.bSelected = true;

                       SelectCard selectCard = new SelectCard();
                       selectCard.cardNo = myHandCard.cardNo;
                       selectCards.add(selectCard);
                   }

                   return true;
               }
            }

            i++;
        }

        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        flag = true;
        new Thread(sendable).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        flag = false;
    }
}
