package com.landlords.famgy.famgylandlords;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.landlords.famgy.famgylandlords.util.BeatHandler;

public class GameActivity extends AppCompatActivity
{
    public DisplayMetrics displayMetrics;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    private Game game;
    public static Handler handlerF;
    public static Handler handlerC;
    boolean bCThreadOk = false;
    boolean bVThreadOk = false;

    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();

            handlerC = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String cmd = (String) msg.obj;
                    Log.i("=== HandlerC ", "received : " + cmd);

                    gameEngine();
                }
            };

            BeatHandler.sendMessage(handlerF, "CThread ok");

            Looper.loop();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        intView();
        game = Game.getGame();

        handlerF = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String cmd = (String) msg.obj;
                Log.i("=== HandlerF ", "received : " + cmd);

                switch (cmd)
                {
                    case "CThread ok":
                        bCThreadOk = true;
                        if (bVThreadOk == true) {
                            BeatHandler.sendMessage(handlerC, "Game start");
                        }
                        break;
                    case "VThread ok":
                        bVThreadOk = true;
                        if (bCThreadOk == true) {
                            BeatHandler.sendMessage(handlerC, "Game start");
                        }
                    default:
                        break;
                }
            }
        };

        new Thread(sendable).start();

        GameView gameView = new GameView(this, handlerF);
        setContentView(gameView);
    }

    private void intView() {
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        SCREEN_WIDTH = displayMetrics.widthPixels;
        SCREEN_HEIGHT = displayMetrics.heightPixels;
    }

    private void gameEngine() {
        switch (game.status)
        {
            case NotStart:
                game.startGame();
                break;
            case GetLandlord:
                game.callLandlord();
                break;
            case SetLandlord:
                game.setLandlord();
                break;
            default:
                break;
        }
    }
}
