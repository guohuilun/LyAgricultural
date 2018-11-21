package com.lyagricultural.utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

/**
 * 作者Administrator on 2018/8/28 0028 17:37
 */
public class GradientDrawableUtils {


    public static  GradientDrawable setGDrawable(int size,String color){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor(color));
        drawable.setCornerRadius(size);
        return drawable;
    }

}
