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
import android.widget.ImageView;

import com.lyagricultural.R;
import com.lyagricultural.utils.LyLog;


public class BreedHiveNameMyDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "BreedHiveNameMyDialogFragment";
    private View breedHiveNameMyDialogView;
    private ImageView cancel_iv;
    private Button startButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        breedHiveNameMyDialogView=inflater.inflate(R.layout.ly_dialog_fragment_breed_hive_name_my,null);
        initView();
        return breedHiveNameMyDialogView;
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
        cancel_iv=breedHiveNameMyDialogView.findViewById(R.id.dialog_fragment_task_cancel_ll);
        startButton= breedHiveNameMyDialogView.findViewById(R.id.startButton);
        cancel_iv.setOnClickListener(this);
        startButton.setOnClickListener(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        LyLog.i(TAG,"执行没得 = onDismiss");
        super.onDismiss(dialog);
    }



    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.cancel_iv:
               getDialog().dismiss();
               break;
           case R.id.startButton:
               break;
       }
    }
}
