package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.EventBusLandDetailsBean;
import com.lyagricultural.bean.ShopDetailBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/6/5 0005 17:24
 */
public class LandDetailsActivity extends BaseActivity {
    private static final String TAG = "LandDetailsActivity";

    private LinearLayout land_details_ll;
    private TextView land_details_name_tv;
    private TextView land_details_address_tv;
    private TextView land_details_time_tv;
    private String goodsId;
    private String endDt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_details);
        setTitle("土地详情");
        initView();
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
            goodsId = intent.getStringExtra("goodsId");
            endDt = intent.getStringExtra("endDt");
        }
        land_details_ll=findViewById(R.id.land_details_ll);
        land_details_name_tv=findViewById(R.id.land_details_name_tv);
        land_details_address_tv=findViewById(R.id.land_details_address_tv);
        land_details_time_tv=findViewById(R.id.land_details_time_tv);
        land_details_time_tv.setText(endDt);
        land_details_ll.setVisibility(View.INVISIBLE);
        initLandName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new EventBusLandDetailsBean("OK"));
    }


    /**
     *  获取商品详情   -网络请求
     */
    private void initLandName(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_GOODS_DETAIL)
                    .addParams("GoodsId",goodsId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,response);
                            Gson gson=new Gson();
                            ShopDetailBean parse=gson.fromJson(response,ShopDetailBean.class);
                            if ("OK".equals(parse.getStatus())){
                                land_details_name_tv.setText(parse.getGoods().getNme()+"/"+parse.getGoods().getArea()+"㎡");
                                land_details_address_tv.setText(parse.getGoods().getRemark1());
                                land_details_ll.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }
}
