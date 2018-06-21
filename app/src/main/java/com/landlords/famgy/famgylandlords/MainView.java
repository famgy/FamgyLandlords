package com.landlords.famgy.famgylandlords;

import android.content.Context;
import android.content.Intent;
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

public class MainView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener{

    boolean flag = true;
    private SurfaceHolder surfaceHolder;
    private Resources resources;
    private Bitmap bitmap_background;
    private float background_w;
    private float background_h;
    private Bitmap bitmap_begin;
    private float begin_w;
    private float begin_h;
    private Context context;


    Runnable sendable = new Runnable ()
    {
        @Override
        public void run ()
        {
            draw();
        }
    };

    public MainView(Context context) {
        super(context);

        this.context = context;
        surfaceHolder = getHolder();

        initBitmaps();

        surfaceHolder.addCallback(this);
        setOnTouchListener(this);
    }

    private void initBitmaps() {
        resources = getResources();
        background_w = MainActivity.SCREEN_WIDTH;
        background_h = MainActivity.SCREEN_HEIGHT;
        begin_w = 200 * MainActivity.SCREEN_WIDTH / 800;
        begin_h = 100 * MainActivity.SCREEN_HEIGHT / 480;

        bitmap_background = BitmapScala.scalamap(BitmapFactory.decodeResource(resources, R.drawable.place), background_w, background_h);
        bitmap_begin = BitmapScala.scalamap(BitmapFactory.decodeResource(resources, R.drawable.begin), begin_w, begin_h);
    }

    private void draw() {
        Rect src = new Rect();
        Rect des = new Rect();

        src.set(0, 0, bitmap_background.getWidth(), bitmap_background.getHeight());
        des.set(0, 0, (int)background_w, (int)background_h);

        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            Paint paint = new Paint();

            canvas.drawBitmap(bitmap_background, src, des, paint);

            canvas.drawBitmap(bitmap_begin,MainActivity.SCREEN_WIDTH / 2 - begin_w / 2, begin_h, null);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean bAction = false;
        float retx = motionEvent.getRawX();
        float rety = motionEvent.getRawY();

        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (retx > (background_w / 2 - begin_w / 2) &&
                    retx < (background_w / 2 + begin_w / 2) &&
                    rety > begin_h &&
                    rety < 2 * begin_h)
                {
                    bAction = true;
                } else {
                    bAction = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (retx > (background_w / 2 - begin_w / 2) &&
                    retx < (background_w / 2 + begin_w / 2) &&
                    rety > begin_h &&
                    rety < 2 * begin_h)
                {
                    Intent intent = new Intent (context, GameActivity.class);
                    context.startActivity(intent);
                }
                bAction = false;
                break;
            default:
                bAction = false;
                break;
        }

        return bAction;
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
