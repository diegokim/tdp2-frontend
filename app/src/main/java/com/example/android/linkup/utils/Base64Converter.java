package com.example.android.linkup.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by diegokim on 9/10/17.
 */

public class Base64Converter {

    private static final String TAG = "Base64Converter";

    /* Convert Bitmap in base 64 string */
    public String bitmapToBase64(Bitmap bitmap) {
        Log.d(TAG, "bitmapToBase64");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    /* Convert base 64 string in bitmap */
    public Bitmap Base64ToBitmap(String base64) {
        Log.d(TAG, "base64ToBitmap");
        byte[] imageAsBytes = Base64.decode(base64.getBytes(),Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = scaleWidth;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public Bitmap resizeBitmap (Bitmap oldBitmap, int newWidth) {

        float aspectRatio = oldBitmap.getWidth() /
                (float) oldBitmap.getHeight();

        int height = Math.round(newWidth / aspectRatio);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                oldBitmap, newWidth, height, false);
        return resizedBitmap;
    }
}
