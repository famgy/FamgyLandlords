package com.landlords.famgy.famgylandlords.util;

import android.os.Handler;
import android.os.Message;

public class BeatHandler {

    public static void sendMessage(Handler handler, String cmd) {
        Message message = Message.obtain();
        message.obj = cmd;
        handler.sendMessage(message);
    }
}
