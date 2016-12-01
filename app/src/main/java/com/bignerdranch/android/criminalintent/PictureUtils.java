package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.annotation.Nullable;

public class PictureUtils {

    @Nullable
    public static Bitmap getScaledBitmap(@Nullable String path, Activity activity) {
        final Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

    @Nullable
    public static Bitmap getScaledBitmap(@Nullable String path, int destWidth, int destHeight) {
        final BitmapFactory.Options srcOptions = new BitmapFactory.Options();
        srcOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, srcOptions);

        float srcWidth = srcOptions.outWidth;
        float srcHeight = srcOptions.outHeight;

        int sampleSize = 1;
        if (srcWidth > destWidth || srcHeight > destHeight) {
            if (srcWidth > srcHeight) {
                sampleSize = Math.round(srcWidth / srcHeight);
            } else {
                sampleSize = Math.round(srcHeight / srcWidth);
            }
        }

        final BitmapFactory.Options destOptions = new BitmapFactory.Options();
        destOptions.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(path, destOptions);
    }
}
