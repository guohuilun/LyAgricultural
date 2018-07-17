package com.lyagricultural.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * 作者Administrator on 2018/7/13 0013 09:33
 */
public class ImageUtils {
    //    给缓存地址添加时间戳
    public static String reSetHeadImageURL(String oldURL) {
        String newURL;
        int position = oldURL.indexOf("?");
        if (position > 0) {
            newURL = oldURL.substring(0, position);
            newURL = newURL + "?" + System.currentTimeMillis();
        } else {
            newURL = oldURL + "?" + System.currentTimeMillis();
        }
        return newURL;
    }

    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap=null;
            }
        }
    }
}
