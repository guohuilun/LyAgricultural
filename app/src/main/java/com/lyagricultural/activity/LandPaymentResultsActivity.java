package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.EventBusDefaultBean;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者Administrator on 2018/7/3 0003 09:19
 */
public class LandPaymentResultsActivity extends BaseActivity  implements View.OnClickListener{
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
        if ("支付结果".equals(result)){
            payment_results_back_button.setText("返回土地库");
            land_payment_results_tip_tv.setVisibility(View.GONE);
            land_payment_results_tv.setText("支付成功");
            iv_default.setImageResource(R.mipmap.ly_land_activity_payment_results);
        }else if ("种植结果".equals(result)){
            payment_results_back_button.setText("返回种子库");
            land_payment_results_tip_tv.setVisibility(View.VISIBLE);
            land_payment_results_tv.setText("种植成功");
            iv_default.setImageResource(R.mipmap.ly_land_activity_payment_results_o);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.payment_results_back_button:
                EventBus.getDefault().post(new EventBusDefaultBean("ShopFragment"));
                finish();
                break;
            case R.id.payment_results_check_order_button:
                EventBus.getDefault().post(new EventBusDefaultBean("ShopFragment"));
                startActivity(new Intent(LandPaymentResultsActivity.this,AccountOrderActivity.class));
                finish();
                break;
            case R.id.payment_results_check_land_button:
                EventBus.getDefault().post(new EventBusDefaultBean("LandFragment"));
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
                EventBus.getDefault().post(new EventBusDefaultBean("ShopFragment"));
                finish();
                break;
        }
        return false;
    }

}
