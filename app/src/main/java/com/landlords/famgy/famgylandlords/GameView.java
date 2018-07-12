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
    private Bitmap bitmap_pass;
    private Bitmap bitmap_discard;
    private float discard_w;
    private float discard_h;
    private Bitmap bitmap_noDiscard;
    private float noDiscard_w;
    private float noDiscard_h;
    private Bitmap bitmap_time;
    private float time_w;
    private float time_h;
    private Bitmap bitmap_win;
    private Bitmap bitmap_fail;
    private float win_w;
    private float win_h;
    private Bitmap bitmap_restart;
    private float restart_w;
    private float restart_h;

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

                    Canvas canvas = surfaceHolder.lockCanvas ();
                    if (canvas != null)
                        synchronized (game.getGame())
                        {
                            drawGame(canvas);
                            //Thread.sleep (30);
                            surfaceHolder.unlockCanvasAndPost (canvas);
                        }
                }
            };

            Log.i("=== handlerF ", "handlerV send : " + "VThread ok");
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
        noDiscard_w = 50 * GameActivity.SCREEN_WIDTH / 800;
        noDiscard_h = 50 * GameActivity.SCREEN_HEIGHT / 480;
        time_w = 50 * GameActivity.SCREEN_WIDTH / 800;
        time_h = 50 * GameActivity.SCREEN_HEIGHT / 480;
        win_w = 400 * GameActivity.SCREEN_WIDTH / 800;
        win_h = 350 * GameActivity.SCREEN_HEIGHT / 480;
        restart_w = 200 * GameActivity.SCREEN_WIDTH / 800;
        restart_h = 100 * GameActivity.SCREEN_HEIGHT / 480;

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
        bitmap_pass = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.pass3), discard_w, discard_h);
        bitmap_discard = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.discard1), discard_w, discard_h);
        bitmap_noDiscard = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.nodiscard), noDiscard_w, noDiscard_h);
        bitmap_time = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.time), time_w, time_h);
        bitmap_win = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.win), win_w, win_h);
        bitmap_fail = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.failed), win_w, win_h);
        bitmap_restart = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.restart), restart_w, restart_h);

        //初始化卡牌
        bitmaps_cards = new Bitmap[54];
        bitmaps_cards_l = new Bitmap[54];
        bitmaps_cards_d = new Bitmap[54];
        for (int i = 0; i < 54; i++)
        {
            Bitmap bitmap_card = BitmapFactory.decodeResource (resources, cardImage.cardImages[i / 4][i % 4]);
            bitmaps_cards[i] = BitmapScala.scalamap (bitmap_card, card_cur_w, card_cur_h);
            bitmaps_cards_l[i] = BitmapScala.scalamap (bitmap_card, card_back_w, card_back_h);
            bitmaps_cards_d[i] = BitmapScala.scalamap (bitmap_card, cards_d_w, cards_d_h);
            //game.select[i] = false;
        }
    }

    private void drawGame(Canvas canvas) {
        Rect src = new Rect();
        Rect des = new Rect();

        src.set(0, 0, bitmap_background.getWidth(), bitmap_background.getHeight());
        des.set(0, 0, GameActivity.SCREEN_WIDTH, GameActivity.SCREEN_HEIGHT);

        Paint paint = new Paint();

        canvas.drawBitmap(bitmap_background, src, des, paint);
        canvas.drawBitmap(bitmap_desk, 50 * GameActivity.SCREEN_WIDTH / 800, 50 * GameActivity.SCREEN_HEIGHT / 480, null);

        canvas.drawBitmap(bitmap_player1, 10 * GameActivity.SCREEN_WIDTH / 800, 326 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap(bitmap_player2, 10 * GameActivity.SCREEN_WIDTH / 800, 25 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap(bitmap_player3, 672 * GameActivity.SCREEN_WIDTH / 800, 5 * GameActivity.SCREEN_HEIGHT / 480, null);

        switch (game.status)
        {
            case GetLandlord:
                drawStart(canvas);
                break;
            case SetLandlord:
                drawLandlord(canvas);
                break;
            case DiscardSelect:
                drawDiscardSelect(canvas);
                break;
            case DiscardSend:
                drawDiscardSend(canvas);
                break;
            case Wait:
                drawDiscardWait(canvas);
                break;
            case GameOver:
                drawGameOver(canvas);
                break;
            default:
                break;
        }
    }

    private void drawStart(Canvas canvas) {
        initPokers(canvas);

        draw_card_loard(canvas);
        draw_button_loard(canvas);
    }

    private void drawLandlord(Canvas canvas) {
        initPokers(canvas);

        draw_card_loard(canvas);
        draw_button_loard(canvas);

        Log.i("=== handlerC ", "drawLandlord send : " + "Set landlord ok");
        BeatHandler.sendMessage(GameActivity.handlerC, "Set landlord ok");
    }

    private void drawDiscardSelect(Canvas canvas) {
        if (game.bFitCards == false) {
            clearSelectCards();
            draw_not_fit(canvas);
            game.bFitCards = true;
        }

        initPokers(canvas);

        draw_card_loard(canvas);
        draw_button_discard(canvas);
        draw_show_cards(canvas);
        draw_curr(canvas);
    }

    private void drawDiscardSend(Canvas canvas) {
        initPokers(canvas);

        draw_card_loard(canvas);
        draw_show_cards(canvas);
        draw_curr(canvas);

        Log.i("=== handlerC ", "drawDiscardSend send : " + "Discard send ok");
        BeatHandler.sendMessage(GameActivity.handlerC, "Discard send ok");
    }

    private void drawDiscardWait(Canvas canvas) {
        initPokers(canvas);

        draw_card_loard(canvas);
        draw_show_cards(canvas);
        draw_curr(canvas);

        Log.i("=== handlerC ", "drawDiscardSend send : " + "Discard wait ok");
        BeatHandler.sendMessage(GameActivity.handlerC, "Discard wait ok");
    }

    private void drawGameOver(Canvas canvas) {
        initPokers(canvas);

        draw_card_loard(canvas);
        draw_show_cards(canvas);
        draw_curr(canvas);

        draw_final_view(canvas);

        Log.i("=== handlerC ", "drawDiscardSend send : " + "Discard finish ok");
        BeatHandler.sendMessage(GameActivity.handlerC, "Discard finish ok");
    }

    private void draw_final_view (Canvas canvas)
    {
        if (game.players[0].myHandCards.isEmpty())
            canvas.drawBitmap (bitmap_win, 200 * GameActivity.SCREEN_WIDTH / 800, 65 * GameActivity.SCREEN_HEIGHT / 480, null);

        else
            canvas.drawBitmap (bitmap_fail, 200 * GameActivity.SCREEN_WIDTH / 800, 65 * GameActivity.SCREEN_HEIGHT / 480, null);

        canvas.drawBitmap (bitmap_restart, 300 * GameActivity.SCREEN_WIDTH / 800, 300 * GameActivity.SCREEN_HEIGHT / 480, null);
    }

    private void draw_curr (Canvas canvas)
    {
        //绘制图标给正在打牌玩家
        int x = 0;
        int y = 0;
        if (game.curPlayer.getNo () == 0)
        {
            x = 100;
            y = 300;
        }
        else if (game.curPlayer.getNo () == 1)
        {
            x = 700;
            y = 120;
        }
        else if (game.curPlayer.getNo () == 2)
        {
            x = 100;
            y = 120;
        }
        canvas.drawBitmap (bitmap_time, x * GameActivity.SCREEN_WIDTH / 800, y * GameActivity.SCREEN_HEIGHT / 480, null);
    }

    private void initPokers(Canvas canvas) {

        ArrayList<MyHandCard> myHandCards = game.players[0].myHandCards;

        Log.e("GameView", "initPokers start ..." + "curStatus :" + game.status);

        for (MyHandCard myHandCard : myHandCards) {
            canvas.drawBitmap (bitmaps_cards[myHandCard.cardNo], myHandCard.retx, myHandCard.rety, null);
        }

        canvas.drawBitmap(bitmap_card_back, 620 * GameActivity.SCREEN_WIDTH / 800, 100 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap(bitmap_card_back, 100 * GameActivity.SCREEN_WIDTH / 800, 100 * GameActivity.SCREEN_HEIGHT / 480, null);

        Paint paint = new Paint ();
        paint.setTextSize (60);
        String str1 = String.valueOf (game.players[1].myHandCards.size());
        String str2 = String.valueOf (game.players[2].myHandCards.size());
        canvas.drawText (str1, 620 * GameActivity.SCREEN_WIDTH / 800, 130 * GameActivity.SCREEN_HEIGHT / 480, paint);
        canvas.drawText (str2, 100 * GameActivity.SCREEN_WIDTH / 800, 130 * GameActivity.SCREEN_HEIGHT / 480, paint);

        Log.e("Game", "initPokers end" + "curStatus :" + game.status);
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
    }

    private void draw_button_loard(Canvas canvas) {
        canvas.drawBitmap(bitmap_no1, 170 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);

        canvas.drawBitmap (bitmap_score11, 290 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap (bitmap_score21, 410 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap (bitmap_score31, 530 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);
    }

    private void draw_button_discard(Canvas canvas) {
        switch (game.curPlayer.passStatus)
        {
            case 1:
                bitmap_pass = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.pass1), discard_w, discard_h);
                break;
            case 2:
                bitmap_pass = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.pass2), discard_w, discard_h);
                break;
            case 3:
                bitmap_pass = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.pass3), discard_w, discard_h);
                break;
            default:
                break;
        }

        canvas.drawBitmap (bitmap_pass, 290 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap (bitmap_discard, 410 * GameActivity.SCREEN_WIDTH / 800, 260 * GameActivity.SCREEN_HEIGHT / 480, null);
    }

    private void draw_show_cards(Canvas canvas) {
        int n = 0;

        /* 自己出牌不显示 “不出” */
        if (game.players[0].passStatus == 3 && game.status == Game.Status.Wait)
        {
            canvas.drawBitmap (bitmap_noDiscard, (300 + 20 * n) * GameActivity.SCREEN_WIDTH / 800, 250 * GameActivity.SCREEN_HEIGHT / 480, null);
        } else {
            for (int i = 0; i < game.players[0].deskCards.size(); i++)
            {
                canvas.drawBitmap (bitmaps_cards_d[game.players[0].deskCards.get(i).cardNo], (300 + 20 * n) * GameActivity.SCREEN_WIDTH / 800, 250 * GameActivity.SCREEN_HEIGHT / 480, null);
                n++;
            }
        }

        n = 0;
        if ((game.players[1].passStatus == 3 && game.status == Game.Status.Wait) ||
                (game.players[1].passStatus == 3 && game.status == Game.Status.DiscardSelect))
        {
            canvas.drawBitmap (bitmap_noDiscard, (500) * GameActivity.SCREEN_WIDTH / 800, 120 * GameActivity.SCREEN_HEIGHT / 480, null);
        } else {
            for (int i = 0; i < game.players[1].deskCards.size(); i++)
            {
                canvas.drawBitmap (bitmaps_cards_d[game.players[1].deskCards.get(i).cardNo], (500 + n * 20) * GameActivity.SCREEN_WIDTH / 800, 120 * GameActivity.SCREEN_HEIGHT / 480, null);
                n++;
            }
        }

        n = 0;
        if ((game.players[2].passStatus == 3 && game.status == Game.Status.Wait) ||
                (game.players[2].passStatus == 3 && game.status == Game.Status.DiscardSelect))
        {
            canvas.drawBitmap (bitmap_noDiscard, (200) * GameActivity.SCREEN_WIDTH / 800, 120 * GameActivity.SCREEN_HEIGHT / 480, null);
        } else {
            for (int i = 0; i < game.players[2].deskCards.size(); i++)
            {
                canvas.drawBitmap (bitmaps_cards_d[game.players[2].deskCards.get(i).cardNo], (200 + n * 20) * GameActivity.SCREEN_WIDTH / 800, 120 * GameActivity.SCREEN_HEIGHT / 480, null);
                n++;
            }
        }
    }

    private void draw_not_fit (Canvas canvas)
    {
        //出牌不符合规则时绘制
        Paint paint = new Paint ();
        paint.setTextSize (70);
        String str = "您选的牌不符";
        canvas.drawText (str, 300 * GameActivity.SCREEN_WIDTH / 800, 200 * GameActivity.SCREEN_HEIGHT / 480, paint);
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

        /* 非本人为curplayer，不允许点击 */
        if (game.status != Game.Status.GetLandlord && game.curPlayer != game.players[0]) {
            return false;
        }

        if (retx > 530 * GameActivity.SCREEN_WIDTH / 800 &&
            retx < 530 * GameActivity.SCREEN_WIDTH / 800 + score_w &&
            rety > 260 * GameActivity.SCREEN_HEIGHT / 480 &&
            rety < 260 * GameActivity.SCREEN_HEIGHT / 480 + score_h &&
            game.status == Game.Status.GetLandlord)
        {
            /* 叫3分 */
            bAction = true;
        }
        else if (rety > 330 * GameActivity.SCREEN_HEIGHT / 480 &&
                rety < 330 * GameActivity.SCREEN_HEIGHT / 480 + card_cur_h &&
                game.status == Game.Status.DiscardSelect)
        {
            if (clickHandCard(retx, rety) == true)
            {
                Log.i("=== handlerV ", "playViewDown send : " + "Discard select");
                BeatHandler.sendMessage(handlerV, "Discard select");
            }

            bAction = false;
        }
        else if (retx > 290 * GameActivity.SCREEN_WIDTH / 800 && retx < 290 * GameActivity.SCREEN_WIDTH / 800 + discard_w &&
                 rety > 260 * GameActivity.SCREEN_HEIGHT / 480 && rety < 260 * GameActivity.SCREEN_HEIGHT / 480 + discard_h &&
                 game.status == Game.Status.DiscardSelect)
        {
            //按钮不出
            game.curPlayer.passStatus = 2;

            clearSelectCards();

            Log.i("=== handlerV ", "playViewDown send : " + "Discard send down");
            BeatHandler.sendMessage(handlerV, "Discard send down");

            bAction = true;
        }
        else if (retx > 410 * GameActivity.SCREEN_WIDTH / 800 && retx < 410 * GameActivity.SCREEN_WIDTH / 800 + discard_w &&
                rety > 260 * GameActivity.SCREEN_HEIGHT / 480 && rety < 260 * GameActivity.SCREEN_HEIGHT / 480 + discard_h &&
                game.status == Game.Status.DiscardSelect)
        {
            /* 出牌 */
            bitmap_discard = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.discard2), discard_w, discard_h);

            Log.i("=== handlerV ", "playViewDown send : " + "Discard send down");
            BeatHandler.sendMessage(handlerV, "Discard send down");

            bAction = true;
        }
        else if (retx > 300 * GameActivity.SCREEN_WIDTH / 800 && retx < 300 * GameActivity.SCREEN_WIDTH / 800 + restart_w &&
                rety > 260 * GameActivity.SCREEN_HEIGHT && rety < 260 * GameActivity.SCREEN_HEIGHT + restart_h &&
                game.status == Game.Status.GameOver)
        {
            Log.i("=== handlerV ", "playViewDown send : " + "restart");
            BeatHandler.sendMessage(handlerV, "Restart");
        }
        return bAction;
    }

    private boolean playViewUp(float retx, float rety) {
        boolean bAction = false;

        if (retx > 530 * GameActivity.SCREEN_WIDTH / 800 &&
                retx < 530 * GameActivity.SCREEN_WIDTH / 800 + score_w &&
                rety > 260 * GameActivity.SCREEN_HEIGHT / 480 &&
                rety < 260 * GameActivity.SCREEN_HEIGHT / 480 + score_h&&
                game.status == Game.Status.GetLandlord)
        {
            /* 叫3分 */
            bitmap_score31 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.score32), score_w, score_h);

            Log.i("=== HandlerC ", "playViewUp send : " + "Call landlord ok");
            BeatHandler.sendMessage(GameActivity.handlerC, "Call landlord ok");

            bAction = true;
        }
        else if (retx > 290 * GameActivity.SCREEN_WIDTH / 800 && retx < 290 * GameActivity.SCREEN_WIDTH / 800 + discard_w &&
                rety > 260 * GameActivity.SCREEN_HEIGHT / 480 && rety < 260 * GameActivity.SCREEN_HEIGHT / 480 + discard_h &&
                game.status == Game.Status.DiscardSelect)
        {
            //按钮不出
            game.curPlayer.passStatus = 3;

            Log.i("=== handlerC ", "playViewUp send : " + "Discard send up");
            BeatHandler.sendMessage(GameActivity.handlerC, "Discard send down");
        }
        else if (retx > 300 * GameActivity.SCREEN_WIDTH / 800 && retx < 410 * GameActivity.SCREEN_WIDTH / 800 + discard_w &&
                rety > 260 * GameActivity.SCREEN_HEIGHT / 480 && rety < 260 * GameActivity.SCREEN_HEIGHT / 480 + discard_h &&
                game.status == Game.Status.GameOver)
        {
            /* 出牌 */
            bitmap_discard = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.discard1), discard_w, discard_h);

            game.players[0].passStatus = 1;

            Log.i("=== HandlerC ", "playViewUp send : " + "Discard send up handlerC");
            BeatHandler.sendMessage(GameActivity.handlerC, "Discard send up handlerC");
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
        MyHandCard myHandCard = null;
        boolean bAction = false;

        for (int i = 0; i < myHandCards.size(); i++) {
            myHandCard = myHandCards.get(i);
            if (i != myHandCards.size() - 1) {
                if (retx > myHandCard.retx && retx < myHandCard.retx + 25 * GameActivity.SCREEN_WIDTH / 800) {
                    if (myHandCard.bSelected == true) {
                        myHandCard.rety = 350 * GameActivity.SCREEN_HEIGHT / 480;
                        myHandCard.bSelected = false;

                        game.removeSelectCardsNode(selectCards, myHandCard.cardNo);
                    } else {
                        myHandCard.rety = 330 * GameActivity.SCREEN_HEIGHT / 480;
                        myHandCard.bSelected = true;

                        SelectCard selectCard = new SelectCard();
                        selectCard.cardNo = myHandCard.cardNo;
                        game.sortAddList(selectCards, selectCard);
                    }

                    bAction = true;
                }
            } else {
                if (retx > myHandCard.retx && retx < myHandCard.retx + discard_w) {
                    if (myHandCard.bSelected == true) {
                        myHandCard.rety = 350 * GameActivity.SCREEN_HEIGHT / 480;
                        myHandCard.bSelected = false;

                        game.removeSelectCardsNode(selectCards, myHandCard.cardNo);
                    } else {
                        myHandCard.rety = 330 * GameActivity.SCREEN_HEIGHT / 480;
                        myHandCard.bSelected = true;

                        SelectCard selectCard = new SelectCard();
                        selectCard.cardNo = myHandCard.cardNo;
                        game.sortAddList(selectCards, selectCard);
                    }

                    bAction = true;
                }
            }
        }

        return bAction;
    }

    private void  clearSelectCards() {
        ArrayList<MyHandCard> myHandCards = game.players[0].myHandCards;
        ArrayList<SelectCard> selectCards = game.players[0].selectCards;
        MyHandCard myHandCard = null;

        for (int i = 0; i < myHandCards.size(); i++) {
            myHandCard = myHandCards.get(i);
            if (myHandCard.bSelected == true) {
                myHandCard.rety = 350 * GameActivity.SCREEN_HEIGHT / 480;
                myHandCard.bSelected = false;

                game.removeSelectCardsNode(selectCards, myHandCard.cardNo);
            }
        }
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
