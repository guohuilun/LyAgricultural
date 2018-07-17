package com.lyagricultural.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者Administrator on 2018/6/14 0014 09:02
 */
public class SpSimpleUtils {

    /**
     * 存储数据
     */
    public static void saveSp(String key, String value,Context context,String msg) {
        SharedPreferences activity_login = context.getSharedPreferences(msg, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = activity_login.edit();
        edit.putString(key, value);
        edit.apply();
    }

    /**
     * 获取数据
     */
    public static  String getSp(String key,Context context,String msg) {
        SharedPreferences activity_login = context.getSharedPreferences(msg, Context.MODE_PRIVATE);
        return activity_login.getString(key, "");
    }

    /**
     * 移除数据
     */
    public static void removeSharedPreference(String key,Context context,String msg) {
        SharedPreferences activity_login = context.getSharedPreferences(msg, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = activity_login.edit();
        edit.remove(key);
        edit.apply();
    }

    /**
     * 判断字符串是否为空
     *
     * @param source
     * @return
     */
    public static boolean TextIsEmpty(String source) {
        if (source == null || "".equals(source)) {
            return true;
        }
        return false;
    }

}
