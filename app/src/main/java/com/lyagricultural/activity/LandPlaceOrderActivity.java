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
public class LandPlaceOrderActivity extends BaseActivity implements View.OnClickListener,DownTimerListener {
    private static final String TAG = "LandPlaceOrderActivity";
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
    private String monthNum;
    private double priceCommit;
    private String nme;
    private String gid;
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
            monthNum=intent.getStringExtra("monthNum");
            priceCommit = intent.getDoubleExtra("priceCommit", 0);
            nme=intent.getStringExtra("nme");
            gid=intent.getStringExtra("gid");
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
        land_place_order_product_tv.setText(nme);
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
                  initOrderCreate();
//                测试用
//                startActivity(new Intent(LandPlaceOrderActivity.this,LandPaymentResultsActivity.class));
//                EventBus.getDefault().post(new EventBusDefaultBean("OK"));
//                finish();
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


    /**
     *  创建订单   -网络请求
     */
    private void initOrderCreate(){
        List<Map<String,String>> goodsList=new ArrayList<>();
        Map<String,String> goodList=new HashMap<>();
        goodList.put("goodsId",gid);
        goodList.put("unitPrice",""+priceCommit);
        goodList.put("goodsNum","1");
        goodsList.add(goodList);
        String s = new Gson().toJson(goodsList);
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_ORDER_CREATE)
                    .addParams("userId", SpSimpleUtils.getSp("userid",LandPlaceOrderActivity.this,"LoginActivity"))
                    .addParams("totalAmt",""+priceCommit)
                    .addParams("payType","ZHYE")
                    .addParams("Remark","")
                    .addParams("landId","")
                    .addParams("goodsList",s)
                    .addParams("goodsType","LAND")
                    .addParams("monthNum",monthNum)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"创建订单 = " +response);
                            Gson gson=new Gson();
                            DefaultBean parse=gson.fromJson(response,DefaultBean.class);
                            if ("OK".equals(parse.getStatus())){
                                LyToast.shortToast(LandPlaceOrderActivity.this,parse.getMsg());
                                startActivity(new Intent(LandPlaceOrderActivity.this,LandPaymentResultsActivity.class)
                                .putExtra("result","支付结果"));
                                EventBus.getDefault().post(new EventBusDefaultBean("OK"));
                                finish();
                            }else {
                                LyToast.shortToast(LandPlaceOrderActivity.this,parse.getMsg());
                            }
                        }
                    });
        }
    }
    
}
