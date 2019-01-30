package com.lyagricultural.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.DefaultBean;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.utils.timer.DownTimerListener;
import com.lyagricultural.view.TextSpan;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/5/30 0030 16:09
 */
public class BreedHiveNamePlaceOrderActivity extends BaseActivity implements View.OnClickListener,DownTimerListener {
    private static final String TAG = "BreedHiveNamePlaceOrderActivity";
    private RelativeLayout land_place_order_rl;
    private ImageView land_place_order_iv;
    private TextView land_place_order_product_tv;
    private TextView land_place_order_balance_tv;
    private TextView land_place_order_consumption_tv;
    private TextView land_place_order_surplus_tv;
    private TextView land_place_order_pay_time_tv;
    private RelativeLayout land_leased_place_order_buy_rl;
    private TextView land_leased_place_order_buy_tv;
    private TextView land_place_order_money_tv;
    private Button land_leased_place_order_buy_button;

    private String title="";
    private double priceCommit;
    private double priceCompare;
//    private DownTimer downTimer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_place_order);
        setTitle("提交订单");
        initView();
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
            title = intent.getStringExtra("text");
            priceCommit = intent.getDoubleExtra("priceCommit", 0);
            priceCompare = intent.getDoubleExtra("priceCompare", 0);
        }
        land_place_order_rl=findViewById(R.id.land_place_order_rl);
        land_place_order_iv=findViewById(R.id.land_place_order_iv);
        land_place_order_product_tv=findViewById(R.id.land_place_order_product_tv);
        land_place_order_balance_tv=findViewById(R.id.land_place_order_balance_tv);
        land_place_order_consumption_tv=findViewById(R.id.land_place_order_consumption_tv);
        land_place_order_surplus_tv=findViewById(R.id.land_place_order_surplus_tv);
        land_place_order_pay_time_tv=findViewById(R.id.land_place_order_pay_time_tv);
        land_leased_place_order_buy_rl=findViewById(R.id.land_leased_place_order_buy_rl);
        land_leased_place_order_buy_tv=findViewById(R.id.land_leased_place_order_buy_tv);
        land_place_order_money_tv=findViewById(R.id.land_place_order_money_tv);
        land_leased_place_order_buy_button=findViewById(R.id.land_leased_place_order_buy_button);
        land_leased_place_order_buy_button.setOnClickListener(this);
        SpannableStringBuilder spannableMoney = new SpannableStringBuilder("货币不足，请充值");
        spannableMoney.setSpan(new TextSpan(),6,8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        land_leased_place_order_buy_tv.setText(spannableMoney);
        SpannableStringBuilder spannablePayTime = new SpannableStringBuilder("请于10:00分钟内完成支付，过时订单自动取消");
        spannablePayTime.setSpan(new TextSpan(){
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.parseColor("#DD2323"));
            }
        },2,7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        land_place_order_pay_time_tv.setText(spannablePayTime);
        land_place_order_money_tv.setText(""+priceCommit);
        land_place_order_product_tv.setText(title);
        land_place_order_consumption_tv.setText("-"+priceCommit);
        land_place_order_balance_tv.setText(""+priceCompare);
        double v = priceCompare - priceCommit;
        land_place_order_surplus_tv.setText(""+v);
//        downTimer=new DownTimer();
//        downTimer.setListener(this);
//        downTimer.startDown(600*1000);
        land_place_order_rl.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.land_leased_place_order_buy_button:
//                测试用
                startActivity(new Intent(this,BreedHiveNamePaymentResultsActivity.class).putExtra("result","申请认领"));
                EventBus.getDefault().post(new EventBusDefaultBean("OK"));
                finish();
                break;
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
      LyLog.i(TAG,"计时器 = "+millisUntilFinished);
    }

    @Override
    public void onFinish() {
           finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        downTimer.stopDown();
    }
}
