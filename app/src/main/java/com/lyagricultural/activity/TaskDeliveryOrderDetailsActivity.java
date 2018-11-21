package com.lyagricultural.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.fragment.TaskOrderDetailsGiveUpDialogFragment;
import com.lyagricultural.utils.WidthUtils;

/**
 * 作者Administrator on 2018/8/27 0027 15:28
 */
public class TaskDeliveryOrderDetailsActivity extends BaseActivity implements View.OnClickListener{
    private TextView task_delivery_order_details_release_tv;
    private TextView task_delivery_order_details_type_tv;
    private TextView task_delivery_order_details_time_tv;
    private RecyclerView task_delivery_order_details_rv;
    private TextView task_delivery_order_details_remarks_tv;

    private  PopupWindow popupWindow;
    private TaskOrderDetailsGiveUpDialogFragment giveUpDialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_task_delivery_order_details);
        initView();
    }

    private void initView(){
        task_delivery_order_details_release_tv=findViewById(R.id.task_delivery_order_details_release_tv);
        task_delivery_order_details_type_tv=findViewById(R.id.task_delivery_order_details_type_tv);
        task_delivery_order_details_time_tv=findViewById(R.id.task_delivery_order_details_time_tv);
        task_delivery_order_details_rv=findViewById(R.id.task_delivery_order_details_rv);
        task_delivery_order_details_remarks_tv=findViewById(R.id.task_delivery_order_details_remarks_tv);
        mRlRight.setOnClickListener(this);
        giveUpDialogFragment=new TaskOrderDetailsGiveUpDialogFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_right:
                showPopupWindow();
                break;
        }
    }

    private void showPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.ly_activity_task_delivery_order_pop, null);
        int screenWidth = WidthUtils.getScreenWidth(this);
        popupWindow=new PopupWindow(view,screenWidth/3, 400,false);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
        view.findViewById(R.id.task_delivery_order_pop_give_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveUpDialogFragment.show(getFragmentManager(),"giveUpDialogFragment");
                popupWindow.dismiss();
            }
        });

        view.findViewById(R.id.task_delivery_order_pop_modify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(mRlRight);

    }

}
