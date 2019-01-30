package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.EventBusDefaultBean;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者Administrator on 2018/7/3 0003 09:19
 */
public class BreedHiveNamePaymentResultsActivity extends BaseActivity  implements View.OnClickListener{
    private ImageView iv_default;
    private TextView land_payment_results_tv;
    private Button payment_results_back_button;
    private Button payment_results_check_order_button;
    private Button payment_results_check_land_button;
    private TextView land_payment_results_tip_tv;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_payment_results);
        mTitle.setGravity(Gravity.CENTER);
        setHeadLeftVisibility(View.INVISIBLE);
        initView();
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
             result = intent.getStringExtra("result");
             setTitle(result);
        }
        iv_default=findViewById(R.id.iv_default);
        land_payment_results_tv=findViewById(R.id.land_payment_results_tv);
        payment_results_back_button=findViewById(R.id.payment_results_back_button);
        payment_results_check_order_button=findViewById(R.id.payment_results_check_order_button);
        payment_results_check_land_button=findViewById(R.id.payment_results_check_land_button);
        land_payment_results_tip_tv=findViewById(R.id.land_payment_results_tip_tv);
        payment_results_back_button.setOnClickListener(this);
        payment_results_check_order_button.setOnClickListener(this);
        payment_results_check_land_button.setOnClickListener(this);
        iv_default.setImageResource(R.drawable.ly_land_activity_payment_results);
        payment_results_back_button.setVisibility(View.GONE);
        payment_results_check_land_button.setText("返回蜂箱");
        land_payment_results_tip_tv.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.payment_results_check_order_button:
                startActivity(new Intent(BreedHiveNamePaymentResultsActivity.this,AccountOrderActivity.class));
                finish();
                break;
            case R.id.payment_results_check_land_button:
                finish();
                break;
        }
    }

    /**
     * 设置系统返回键
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return false;
    }

}
