package com.lyagricultural.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;

/**
 * 作者Administrator on 2018/8/27 0027 15:28
 */
public class TaskDeliveryOrderActivity extends BaseActivity {
    private TextView task_delivery_order_time_tv;
    private EditText task_delivery_order_plucking_et;
    private EditText task_delivery_order_packing_et;
    private TextView task_delivery_order_address_tv;
    private EditText task_delivery_order_weight_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_task_delivery_order);
        initView();
    }

    private void initView(){
        task_delivery_order_time_tv=findViewById(R.id.task_delivery_order_time_tv);
        task_delivery_order_plucking_et=findViewById(R.id.task_delivery_order_plucking_et);
        task_delivery_order_packing_et=findViewById(R.id.task_delivery_order_packing_et);
        task_delivery_order_address_tv=findViewById(R.id.task_delivery_order_address_tv);
        task_delivery_order_weight_et=findViewById(R.id.task_delivery_order_weight_et);
    }

}
