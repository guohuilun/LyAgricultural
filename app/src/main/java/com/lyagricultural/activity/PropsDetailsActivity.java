package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.ShopDetailBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/6/1 0001 15:29
 */
public class PropsDetailsActivity extends BaseActivity {
    private static final String TAG = "PropsDetailsActivity";
    private LinearLayout props_details_ll;
    private ImageView props_details_iv;
    private TextView props_details_land_name_tv;
    private TextView props_details_scene_tv;
    private String goodsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_props_details);
        initView();
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
            String propName = intent.getStringExtra("propName");
            setTitle(propName);
            goodsId = intent.getStringExtra("goodsId");
        }
        props_details_ll=findViewById(R.id.props_details_ll);
        props_details_iv=findViewById(R.id.props_details_iv);
        props_details_land_name_tv=findViewById(R.id.props_details_land_name_tv);
        props_details_scene_tv=findViewById(R.id.props_details_scene_tv);
        initPropsDetails();
    }

    /**
     *  获取商品详情   -网络请求
     */
    private void initPropsDetails(){
        props_details_ll.setVisibility(View.GONE);
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
                                if (parse.getImglist().size()>0){
                                  if (!"".equals(parse.getImglist().get(0).getImgUrl())){
                                    Glide.with(PropsDetailsActivity.this).load(parse.getImglist().get(0).getImgUrl()).into(props_details_iv);
                                 }
                               }
                                props_details_land_name_tv.setText(parse.getGoods().getRemark());
                                props_details_scene_tv.setText(parse.getGoods().getRemark1());
                                props_details_ll.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }
    
}
