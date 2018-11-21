package com.lyagricultural.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lyagricultural.R;
import com.lyagricultural.utils.LyLog;

/**
 * 作者Administrator on 2018/6/5 0005 17:27
 */
public class TaskOrderDetailsGiveUpDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "TaskOrderDetailsGiveUpDialogFragment";
    private View taskOrderDetailsGiveUpDialogView;
    private LinearLayout dialog_fragment_task_cancel_ll;
    private EditText dialog_fragment_task_et;
    private Button dialog_fragment_task_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        taskOrderDetailsGiveUpDialogView=inflater.inflate(R.layout.ly_dialog_fragment_task_order_details_give_up,null);
        initView();
        return taskOrderDetailsGiveUpDialogView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }

    private void initView(){
        dialog_fragment_task_cancel_ll=taskOrderDetailsGiveUpDialogView.findViewById(R.id.dialog_fragment_task_cancel_ll);
        dialog_fragment_task_et=taskOrderDetailsGiveUpDialogView.findViewById(R.id.dialog_fragment_task_et);
        dialog_fragment_task_button=taskOrderDetailsGiveUpDialogView.findViewById(R.id.dialog_fragment_task_button);
        dialog_fragment_task_cancel_ll.setOnClickListener(this);
        dialog_fragment_task_button.setOnClickListener(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        LyLog.i(TAG,"执行没得 = onDismiss");
        super.onDismiss(dialog);
    }



    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.dialog_fragment_task_cancel_ll:
               break;
           case R.id.dialog_fragment_task_button:
               break;
       }
    }
}
