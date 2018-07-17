package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.bean.ShopDetailBean;
import com.lyagricultural.bean.ShopFragmentBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.BannerUtils;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/5/29 0029 17:37
 */
public class LandNameActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "LandNameActivity";
    private RelativeLayout land_name_rl;
    private Banner land_name_banner;
    private TextView land_name_address_tv;
    private TextView land_name_size_tv;
    private TextView land_name_describe_tv;
    private TextView land_name_money_tv;
    private Button land_name_rent_button;
    private String goodsId;
    private  List<Integer> ImageData = new ArrayList<>();
    private List<String> ImageGoodData=new ArrayList<>();
    private String nme;
    private String area;
    private String price;
    private String gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_name);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
            String landName = intent.getStringExtra("landName");
            setTitle(landName);
            goodsId = intent.getStringExtra("goodsId");
        }
        land_name_rl=findViewById(R.id.land_name_rl);
        land_name_banner=findViewById(R.id.land_name_banner);
        land_name_address_tv=findViewById(R.id.land_name_address_tv);
        land_name_size_tv=findViewById(R.id.land_name_size_tv);
        land_name_describe_tv=findViewById(R.id.land_name_describe_tv);
        land_name_money_tv=findViewById(R.id.land_name_money_tv);
        land_name_rent_button=findViewById(R.id.land_name_rent_button);
        land_name_rent_button.setOnClickListener(this);
        initLandName();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.land_name_rent_button:
                startActivity(new Intent(LandNameActivity.this,LandLeasedActivity.class)
                .putExtra("nme",nme)
                .putExtra("area",area)
                .putExtra("price",price)
                .putExtra("gid",gid)
                );
                break;
        }
    }




    private void setBanner(){
        land_name_banner.setDelayTime(3000)
                .setImages(ImageGoodData)
                .setImageLoader(new BannerUtils.GlideImageLoader())
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        land_name_banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }

    /**
     *  获取商品详情   -网络请求
     */
    private void initLandName(){
        land_name_rl.setVisibility(View.GONE);
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
                                if (parse.getImglist().size()>0&&parse.getImglist()!=null){
                                    ImageGoodData.clear();
                                    for (int i = 0; i <parse.getImglist().size() ; i++) {
                                        ImageGoodData.add(parse.getImglist().get(i).getImgUrl());
                                    }
                                    setBanner();
                                }
                                 nme = parse.getGoods().getNme();
                                area = parse.getGoods().getArea();
                                price = parse.getGoods().getPrice();
                                gid=parse.getGoods().getGId();
                                land_name_address_tv.setText(parse.getGoods().getRemark1());
                                land_name_size_tv.setText(parse.getGoods().getArea()+"㎡");
                                land_name_describe_tv.setText(parse.getGoods().getRemark());
                                land_name_money_tv.setText("总价："+parse.getGoods().getPrice());
                                land_name_rl.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sentParsms(EventBusDefaultBean bean) {//此方法类似于广播，任何地方都可以传递
        if ("OK".equals(bean.getMsg())){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 测试数据
     */
    private List<Integer> setImageData(){
        ImageData.clear();
        ImageData.add(R.mipmap.ce_shop_land_bg_o);
        ImageData.add(R.mipmap.ce_shop_land_bg_t);
        return ImageData;
    }

}
