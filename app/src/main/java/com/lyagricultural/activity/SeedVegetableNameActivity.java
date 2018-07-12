package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.ShopDetailBean;
import com.lyagricultural.bean.ShopSeedBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.gen.ShopSeedDao;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.BannerUtils;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/5/31 0031 17:26
 */
public class SeedVegetableNameActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SeedVegetableNameActivity";
    private RelativeLayout seed_vegetable_name_rl;
    private Banner seed_vegetable_name_banner;
    private TextView seed_vegetable_name_introduce_tv;
    private TextView seed_vegetable_name_effect_tv;
    private TextView seed_vegetable_name_nutrition_tv;
    private TextView seed_vegetable_name_cycle_tv;
    private TextView seed_vegetable_name_acreage_tv;
    private Button seed_vegetable_name_add_button;
    private String goodsId;
    private List<String> ImageGoodData=new ArrayList<>();
    private  List<Integer> ImageData = new ArrayList<>();
    private String gId;
    private String nme;
    private String seedUrl;
    private String price;
    private String area;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_seed_vegetable_name);
        initView();
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
            String seedName = intent.getStringExtra("seedName");
            setTitle(seedName);
            goodsId = intent.getStringExtra("goodsId");
            seedUrl = intent.getStringExtra("seedUrl");
        }
        seed_vegetable_name_rl=findViewById(R.id.seed_vegetable_name_rl);
        seed_vegetable_name_banner=findViewById(R.id.seed_vegetable_name_banner);
        seed_vegetable_name_introduce_tv=findViewById(R.id.seed_vegetable_name_introduce_tv);
        seed_vegetable_name_effect_tv=findViewById(R.id.seed_vegetable_name_effect_tv);
        seed_vegetable_name_nutrition_tv=findViewById(R.id.seed_vegetable_name_nutrition_tv);
        seed_vegetable_name_cycle_tv=findViewById(R.id.seed_vegetable_name_cycle_tv);
        seed_vegetable_name_acreage_tv=findViewById(R.id.seed_vegetable_name_acreage_tv);
        seed_vegetable_name_add_button=findViewById(R.id.seed_vegetable_name_add_button);
        seed_vegetable_name_add_button.setOnClickListener(this);
        initSeedVegetableName();
        setBanner();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.seed_vegetable_name_add_button:
//                startActivity(new Intent(SeedVegetableNameActivity.this,SeedParcelActivity.class));
                ShopSeedBean shopSeedBean=new ShopSeedBean();
                shopSeedBean.setGId(gId);
                shopSeedBean.setNme(nme);
                shopSeedBean.setUrl(seedUrl);
                shopSeedBean.setPrice(price);
                shopSeedBean.setTip(area);
                ShopSeedDao.insert(shopSeedBean);
                LyToast.shortToast(this,"加入包裹成功");
                break;
        }
    }


    private void setBanner(){
        seed_vegetable_name_banner.setDelayTime(3000)
                .setImages(ImageGoodData)
                .setImageLoader(new BannerUtils.GlideImageLoader())
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
    }


    /**
     *  获取商品详情   -网络请求
     */
    private void initSeedVegetableName(){
        seed_vegetable_name_rl.setVisibility(View.GONE);
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
                                 gId = parse.getGoods().getGId();
                                 nme = parse.getGoods().getNme();
                                 price = parse.getGoods().getPrice();
                                 area = parse.getGoods().getArea();

                                if (parse.getImglist().size()>0){
                                    ImageGoodData.clear();
                                    for (int i = 0; i <parse.getImglist().size() ; i++) {
                                        ImageGoodData.add(parse.getImglist().get(i).getImgUrl());
                                    }
                                }

                                seed_vegetable_name_introduce_tv.setText(parse.getGoods().getRemark());
                                seed_vegetable_name_effect_tv.setText(parse.getGoods().getRemark3());
                                seed_vegetable_name_nutrition_tv.setText(parse.getGoods().getRemark1());
                                seed_vegetable_name_cycle_tv.setText(parse.getGoods().getRemark2()+"天");
                                seed_vegetable_name_acreage_tv.setText(parse.getGoods().getArea()+"㎡/株");
                                seed_vegetable_name_rl.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }


    private List<Integer> setImageData(){
        ImageData.clear();
        ImageData.add(R.mipmap.ce_seed_v_name_banner_o);
        ImageData.add(R.mipmap.ce_seed_v_name_banner_t);
        ImageData.add(R.mipmap.ce_seed_v_name_banner_tt);
        ImageData.add(R.mipmap.ce_seed_v_name_banner_f);
        return ImageData;
    }


}
