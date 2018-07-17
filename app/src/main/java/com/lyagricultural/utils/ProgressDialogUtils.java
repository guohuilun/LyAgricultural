package com.lyagricultural.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 作者Administrator on 2018/7/13 0013 09:01
 */
public class ProgressDialogUtils {

    public static ProgressDialog progressDialog;

    /**
     * 显示加载进度框
     */
    public static void showProgressDialog(Context context,String msg) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        progressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 关闭加载进度框
     */
    public static void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

}
