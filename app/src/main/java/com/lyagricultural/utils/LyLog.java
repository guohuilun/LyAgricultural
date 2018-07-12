package com.lyagricultural.utils;

import android.util.Log;

/**
 * 作者Administrator on 2018/5/22 0022 10:02
 */
public class LyLog {
    private static final String TAG = "LyLog";

    private static final boolean DEBUG=true;

    public LyLog() {
    }

    public static void v(String tag,String msg){
        if (DEBUG){
            Log.v("LyLog", "[ " + tag + " ]" + msg);
        }
    }

    public static void d(String tag,String msg){
        if (DEBUG) {
             Log.d("LyLog", "[ " + tag + " ]" + msg);
        }
    }

    public static void i(String tag,String msg){
        if (DEBUG) {
             Log.i("LyLog", "[ " + tag + " ]" + msg);
        }
    }
    public static void w(String tag,String msg){
        if (DEBUG){
            Log.w("LyLog", "[ " + tag + " ]" + msg);
        }
    }

    public static void e(String tag,String msg){
        if (DEBUG){
             Log.e("LyLog", "[ " + tag + " ]" + msg);
        }
    }

}
