package com.lyagricultural.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.bean.ShopDetailBean;
import com.lyagricultural.bean.ShopSeedBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.gen.ShopSeedDao;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.Arith;
import com.lyagricultural.utils.BannerUtils;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.view.amount.AmountView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private AmountView seed_vegetable_name_all_av;
    private TextView seed_vegetable_name_all_area_tv;
    private Button seed_vegetable_name_add_button;
    private String goodsId;
    private List<String> ImageGoodData=new ArrayList<>();
    private  List<Integer> ImageData = new ArrayList<>();
    private String gId;
    private String nme;
    private String seedUrl;
    private String price;
    private String area;
    private int count=1;
    private double allPrice=0;
    private List<ShopSeedBean> mSeedNewList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_seed_vegetable_name);
        initView();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        ImmersionBar.with(this)
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
                //  .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                //                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //软键盘自动弹出
                .init();
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
        seed_vegetable_name_all_av=findViewById(R.id.seed_vegetable_name_all_av);
        seed_vegetable_name_all_area_tv=findViewById(R.id.seed_vegetable_name_all_area_tv);
        seed_vegetable_name_add_button.setOnClickListener(this);
        initSeedVegetableName();
        setAmount();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.seed_vegetable_name_add_button:
//                ShopSeedBean shopSeedBean=new ShopSeedBean();
//                shopSeedBean.setGId(gId);
//                shopSeedBean.setNme(nme);
//                shopSeedBean.setUrl(seedUrl);
//                shopSeedBean.setPrice(price);
//                shopSeedBean.setTip(area);
//                shopSeedBean.setCount(count);
//                ShopSeedDao.insert(shopSeedBean);
                mSeedNewList.clear();
                ShopSeedBean shopSeedBean=new ShopSeedBean();
                shopSeedBean.setGId(gId);
                shopSeedBean.setNme(nme);
                shopSeedBean.setUrl(seedUrl);
                shopSeedBean.setPrice(price);
                shopSeedBean.setTip(area);
                shopSeedBean.setCount(count);
                mSeedNewList.add(shopSeedBean);
                EventBus.getDefault().postSticky(mSeedNewList);
                 allPrice = Arith.mul(Double.parseDouble(price), (double) count);
                startActivity(new Intent(SeedVegetableNameActivity.this,SeedPlantActivity.class)
                .putExtra("nme",nme)
                .putExtra("price",price)
                .putExtra("area",area)
                .putExtra("count",count)
                .putExtra("allPrice",String.valueOf(allPrice))
                );
//                LyToast.shortToast(this,"加入包裹成功");
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
                            LyLog.i(TAG,"获取商品详情 = "+response);
                            Gson gson=new Gson();
                            ShopDetailBean parse=gson.fromJson(response,ShopDetailBean.class);
                            if ("OK".equals(parse.getStatus())){
                                 gId = parse.getGoods().getGId();
                                 nme = parse.getGoods().getNme();
                                 price = parse.getGoods().getPrice();
                                 area = parse.getGoods().getArea();

                                if (parse.getImglist().size()>0&&parse.getImglist()!=null){
                                    ImageGoodData.clear();
                                    for (int i = 0; i <parse.getImglist().size() ; i++) {
                                        ImageGoodData.add(parse.getImglist().get(i).getImgUrl());
                                    }
                                    setBanner();
                                }

                                seed_vegetable_name_introduce_tv.setText(parse.getGoods().getRemark());
                                seed_vegetable_name_effect_tv.setText(parse.getGoods().getRemark3());
                                seed_vegetable_name_nutrition_tv.setText(parse.getGoods().getRemark1());
                                seed_vegetable_name_cycle_tv.setText(parse.getGoods().getRemark2()+"天");
                                seed_vegetable_name_acreage_tv.setText(parse.getGoods().getArea()+"㎡/株");
                                double areaAmount = Arith.mul(Double.parseDouble(area), (double) count);
                                seed_vegetable_name_all_area_tv.setText(String.valueOf(areaAmount));
                                seed_vegetable_name_rl.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }

    private void setAmount(){
        seed_vegetable_name_all_av.setBgButton(R.drawable.ly_view_amount_view_two_button_left
                ,R.drawable.ly_view_amount_view_two_button_right
                ,R.drawable.ly_view_amount_view_two_button);
        seed_vegetable_name_all_av.setGoods_storage(200);
        seed_vegetable_name_all_av.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                double areaAmount = Arith.mul(Double.parseDouble(area), (double) amount);
                seed_vegetable_name_all_area_tv.setText(String.valueOf(areaAmount));
                count=amount;
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sentParsms(EventBusDefaultBean bean) {//此方法类似于广播，任何地方都可以传递
        if ("SeedOK".equals(bean.getMsg())){
            finish();
        }
    }


    /**
     * 点击外部隐藏软键盘
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);

    }


    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
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
