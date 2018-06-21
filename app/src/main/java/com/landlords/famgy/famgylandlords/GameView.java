package com.landlords.famgy.famgylandlords;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.landlords.famgy.famgylandlords.util.BitmapScala;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener{

    boolean flag = true;
    private SurfaceHolder surfaceHolder;
    private Resources resources;
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

    private Context context;


    Runnable sendable = new Runnable ()
    {
        @Override
        public void run ()
        {
            draw();
        }
    };

    public GameView(Context context) {
        super(context);

        this.context = context;
        surfaceHolder = getHolder();

        initBitmaps();

        surfaceHolder.addCallback(this);
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

        bitmap_background = BitmapScala.scalamap(BitmapFactory.decodeResource(resources, R.drawable.place), background_w, background_h);
        bitmap_desk = BitmapScala.scalamap(BitmapFactory.decodeResource (resources, R.drawable.desk), desk_w, desk_h);
        bitmap_player1 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.player1), player1_w, player1_h);
        bitmap_player2 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.player2), player2_w, player2_h);
        bitmap_player3 = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.player3), player3_w, player3_h);
        bitmap_card_back = BitmapScala.scalamap (BitmapFactory.decodeResource (resources, R.drawable.card_back), card_back_w, card_back_h);
    }

    private void draw() {
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

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void initPokers(Canvas canvas) {

    }

    private void draw_card_loard(Canvas canvas) {
        canvas.drawBitmap (bitmap_card_back, 320 * GameActivity.SCREEN_WIDTH / 800, 20 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap (bitmap_card_back, 380 * GameActivity.SCREEN_WIDTH / 800, 20 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap (bitmap_card_back, 440 * GameActivity.SCREEN_WIDTH / 800, 20 * GameActivity.SCREEN_HEIGHT / 480, null);

        canvas.drawBitmap (bitmap_card_back, 620 * GameActivity.SCREEN_WIDTH / 800, 100 * GameActivity.SCREEN_HEIGHT / 480, null);
        canvas.drawBitmap (bitmap_card_back, 100 * GameActivity.SCREEN_WIDTH / 800, 100 * GameActivity.SCREEN_HEIGHT / 480, null);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
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
