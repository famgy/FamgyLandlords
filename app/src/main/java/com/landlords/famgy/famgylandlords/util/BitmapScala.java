package com.landlords.famgy.famgylandlords.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapScala {

    public static Bitmap scalamap(Bitmap bitmap, float w, float h) {
        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = w / (float)width;
        float scaleHeight = h / (float)height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
