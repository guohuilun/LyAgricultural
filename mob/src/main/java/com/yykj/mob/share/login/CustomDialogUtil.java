package com.yykj.mob.share.login;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yykj.mob.R;


/**
 * 自定义对话框
 * Created by zxb on 2016/4/12.
 */
public class CustomDialogUtil {

    /**
     * 加载进度框
     *
     * @param context
     * @param msg     提示信息
     * @return
     */
    public static Dialog showLoadDialog(Context context, String msg) {
        Dialog dialog = new Dialog(context, R.style.DialogThemeNoTitle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(view);
        dialog.show();
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        TextView tv_msg = (TextView) view.findViewById(R.id.message);
        tv_msg.setText(msg);
        return dialog;
    }
}
