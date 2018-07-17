package com.lyagricultural.utils;

/**
 * 作者Administrator on 2018/7/16 0016 10:15
 */
public class TxUtils {
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
