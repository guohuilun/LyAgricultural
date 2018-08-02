package com.lyagricultural.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.DefaultBean;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.bean.LandFragmentBean;
import com.lyagricultural.bean.PropGoodsDefaultBean;
import com.lyagricultural.bean.ShopSeedBean;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.gen.ShopSeedDao;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.Arith;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.view.TextSpan;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/5/31 0031 15:10
 */
public class SeedPlantActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "SeedPlantActivity";
    private ScrollView seed_plant_sv;
    private RecyclerView seed_plant_seed_rv;
    private RecyclerView seed_plant_seed_land_rv;
    private TextView seed_plant_covered_tv;
    private RecyclerView seed_plant_seed_meal_rv;
    private TextView seed_plant_notes_tv;
    private EditText seed_plant_remarks_tv;
    private TextView seed_plant_remind_tv;
    private RelativeLayout seed_plant_buy_rl;
    private TextView seed_plant_money_tv_buy_tv;
    private TextView seed_plant_money_tv;
    private Button seed_plant_buy_button;
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;

    private BaseRecyclerAdapter<PropGoodsDefaultBean.GoodslistBean> goodslistBeanBaseRecyclerAdapter;
    private List<PropGoodsDefaultBean.GoodslistBean> goodsBeanList;
    private Boolean isShowGoods=false;
    private int showGoodsPosition=0;

    private List<ShopSeedBean> mSeedNewList=new ArrayList<>();
    private BaseRecyclerAdapter<ShopSeedBean> shopSeedBeanBaseRecyclerAdapter;

    private List<LandFragmentBean.LaninfoBean> mLandInfoList;
    private BaseRecyclerAdapter<LandFragmentBean.LaninfoBean> mLandInfoRecyclerAdapter;
    private Boolean isShowLand=false;
    private int showLandPosition=0;

    private String allPrice;

    private int allCount=0;

    private double checkPrice=0;

    private double priceCompare=0;

    private  String landId="";

    private TextView seed_plant_rv_item_name_tv;
    private TextView seed_plant_rv_item_money_tv;
    private String price;
    private String area;
    private int count;
    private String nme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_seed_plant);
        initView();
        setTitle("种植");
        if (!EventBus.getDefault().isRegistered(this)){
            // 以Sticky的形式注册
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
             allPrice = intent.getStringExtra("allPrice");
             nme = intent.getStringExtra("nme");
             price = intent.getStringExtra("price");
             area = intent.getStringExtra("area");
             count=intent.getIntExtra("count",1);
        }
        seed_plant_sv=findViewById(R.id.seed_plant_sv);
        seed_plant_sv.setVisibility(View.INVISIBLE);
        seed_plant_seed_rv=findViewById(R.id.seed_plant_seed_rv);
        seed_plant_seed_land_rv=findViewById(R.id.seed_plant_seed_land_rv);
        seed_plant_covered_tv=findViewById(R.id.seed_plant_covered_tv);
        seed_plant_seed_meal_rv=findViewById(R.id.seed_plant_seed_meal_rv);
        seed_plant_notes_tv=findViewById(R.id.seed_plant_notes_tv);
        seed_plant_remarks_tv=findViewById(R.id.seed_plant_remarks_tv);
        seed_plant_remind_tv=findViewById(R.id.seed_plant_remind_tv);
        seed_plant_buy_rl=findViewById(R.id.seed_plant_buy_rl);
        seed_plant_money_tv_buy_tv=findViewById(R.id.seed_plant_money_tv_buy_tv);
        seed_plant_money_tv=findViewById(R.id.seed_plant_money_tv);
        seed_plant_buy_button=findViewById(R.id.seed_plant_buy_button);
        seed_plant_buy_button.setOnClickListener(this);
        seed_plant_remind_tv.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder spannableRemind = new SpannableStringBuilder("由于当地气温季节等原因，您选择播种的西瓜种子X5可能无法生长，建议您重新选择播种种子，以免给您造成损失！");
//   Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标
//   Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
//   Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终了下标，但都不包括起始下标和终了下标
//   Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终了下标，包括终了下标
        spannableRemind.setSpan(new TextSpan(){
            @Override
            public void onClick(View view) {

            }
        },18,18+6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        seed_plant_remind_tv.setText(spannableRemind);
        SpannableStringBuilder spannableMoney = new SpannableStringBuilder("货币不足，请充值");
        spannableMoney.setSpan(new TextSpan(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SeedPlantActivity.this,AccountRechargeActivity.class));
            }
        },6,8,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        seed_plant_money_tv_buy_tv.setText(spannableMoney);
        seed_plant_money_tv_buy_tv.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        seed_plant_seed_rv.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager2=new LinearLayoutManager(this);
        seed_plant_seed_land_rv.setLayoutManager(layoutManager2);

        RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(this);
        seed_plant_seed_meal_rv.setLayoutManager(layoutManager1);

        seed_plant_covered_tv.setText("占地约0㎡");
        seed_plant_money_tv.setText(allPrice);

        seed_plant_rv_item_name_tv=findViewById(R.id.seed_plant_rv_item_name_tv);
        seed_plant_rv_item_money_tv=findViewById(R.id.seed_plant_rv_item_money_tv);
        double priceOne=Arith.mul(Double.parseDouble(price), (double) count);
        double areaOne=Arith.mul(Double.parseDouble(area), (double) count);
        seed_plant_rv_item_name_tv.setText(nme+"*"+count+"("+String.valueOf(areaOne)+"㎡)");
        seed_plant_rv_item_money_tv.setText(String.valueOf(priceOne));

        initSeedPlant();
        setGoodsBeanList();
//        setSeedListRv();
        initLandFragment();
        setLandFragment();
        initTipSelect();
        seed_plant_sv.setVisibility(View.VISIBLE);
//        setLandRv();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.seed_plant_buy_button:
                double v = Double.parseDouble(seed_plant_money_tv.getText().toString());
                LyLog.i(TAG,"余额 多少= "+priceCompare);
                if (v>priceCompare){
                    seed_plant_buy_rl.setVisibility(View.VISIBLE);
                    LyToast.shortToast(this,"余额不足");
                    return;
                }else {
                    seed_plant_buy_rl.setVisibility(View.GONE);
                }

                if ("".equals(landId)){
                    LyToast.shortToast(this,"请选择播种土地");
                    return;
                }

                initOrderCreate();
//                测试用
//                startActivity(new Intent(SeedPlantActivity.this,LandPaymentResultsActivity.class)
//                        .putExtra("result","种植结果"));
//                EventBus.getDefault().post(new EventBusDefaultBean("SeedOK"));
//                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void sentParsms(List<ShopSeedBean> mSeedList) {//此方法类似于广播，任何地方都可以传递
        mSeedNewList.clear();
        mSeedNewList.addAll(mSeedList);
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


    @Override
    protected void onResume() {
        super.onResume();
        initAccountData();
    }


        @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     *  获取道具商品列表  -网络请求
     */
    private void initSeedPlant(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_PROP_GOODS)
                    .addParams("TypeId","SetMeal")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取道具商品列表 = " +response);
                            Gson gson=new Gson();
                            PropGoodsDefaultBean parse=gson.fromJson(response,PropGoodsDefaultBean.class);
                            if ("OK".equals(parse.getStatus())){
                               goodsBeanList.clear();
                               goodsBeanList.addAll(parse.getGoodslist());
                               goodslistBeanBaseRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    private void setGoodsBeanList(){
        goodsBeanList=new ArrayList<>();
        goodslistBeanBaseRecyclerAdapter=new BaseRecyclerAdapter<PropGoodsDefaultBean.GoodslistBean>
                (this,goodsBeanList,R.layout.ly_activity_seed_plant_meal_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, PropGoodsDefaultBean.GoodslistBean goodslistBean, int position) {
                  holder.setTxt(R.id.seed_plant_meal_rv_tv,goodslistBean.getNme());
                  holder.setTxt(R.id.seed_plant_meal_rv_one_tv,goodslistBean.getTip());
                  holder.setTxt(R.id.seed_plant_meal_money_rv_tv,goodslistBean.getPrice());
                  holder.setClick(R.id.seed_plant_meal_rv_rl,goodslistBean,position,goodslistBeanBaseRecyclerAdapter);
                  if (isShowGoods){
                      if (showGoodsPosition==position){
                          if (goodslistBean.getChecked()){
                              goodslistBean.setChecked(true);
                              holder.setCheck(R.id.seed_plant_meal_rv_check_box,true);
                              double mul = Arith.mul((double) allCount, Double.parseDouble(goodslistBean.getPrice()));
                              checkPrice = mul+Double.parseDouble(allPrice);
//                              checkPrice = allCount * Double.parseDouble(goodslistBean.getPrice()) + Double.parseDouble(allPrice);
                              seed_plant_money_tv.setText(String.valueOf(checkPrice));
                          }else {
                              goodslistBean.setChecked(false);
                              holder.setCheck(R.id.seed_plant_meal_rv_check_box,false);
                              seed_plant_money_tv.setText(allPrice);
                          }
                      }else {
                          holder.setCheck(R.id.seed_plant_meal_rv_check_box,false);
                          goodslistBean.setChecked(false);
                      }
                  }
            }

            @Override
            public void clickEvent(int viewId, PropGoodsDefaultBean.GoodslistBean goodslistBean, int position) {
                super.clickEvent(viewId, goodslistBean, position);
                switch (viewId){
                    case R.id.seed_plant_meal_rv_rl:
                        isShowGoods=true;
                        showGoodsPosition=position;
                        goodslistBean.setChecked(!goodslistBean.getChecked());
                        notifyDataSetChanged();
                        break;
                }
            }
        };
        seed_plant_seed_meal_rv.setAdapter(goodslistBeanBaseRecyclerAdapter);
    }

    /**
     * 设置种子列表
     */
    private void setSeedListRv(){
        shopSeedBeanBaseRecyclerAdapter=new BaseRecyclerAdapter<ShopSeedBean>(this,mSeedNewList,R.layout.ly_activity_seed_plant_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, ShopSeedBean shopSeedBean, int position) {
                allCount +=shopSeedBean.getCount();
//                double price = Double.parseDouble(shopSeedBean.getPrice());
//                double tip = Double.parseDouble(shopSeedBean.getTip());
//                holder.setTxt(R.id.seed_plant_rv_item_name_tv,shopSeedBean.getNme()+"*"+shopSeedBean.getCount()+"("+tip*shopSeedBean.getCount()+"㎡)");
//                holder.setTxt(R.id.seed_plant_rv_item_money_tv,price*shopSeedBean.getCount()+"");
                double price=Arith.mul(Double.parseDouble(shopSeedBean.getPrice()), (double) shopSeedBean.getCount());
                double tip=Arith.mul(Double.parseDouble(shopSeedBean.getTip()), (double) shopSeedBean.getCount());
                holder.setTxt(R.id.seed_plant_rv_item_name_tv,shopSeedBean.getNme()+"*"+shopSeedBean.getCount()+"("+String.valueOf(tip)+"㎡)");
                holder.setTxt(R.id.seed_plant_rv_item_money_tv,String.valueOf(price));
                if (position==mSeedNewList.size()-1){
                    holder.setInVisibility(R.id.seed_plant_rv_item_view,View.GONE);
                }
            }
        };
        seed_plant_seed_rv.setAdapter(shopSeedBeanBaseRecyclerAdapter);
        shopSeedBeanBaseRecyclerAdapter.notifyDataSetChanged();
    }


    /**
     *  获取用户土地  -网络请求
     */
    private void initLandFragment(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_LAND_SELECT)
                    .addParams("userId", SpSimpleUtils.getSp("userid",this,"LoginActivity"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取用户土地 = " +response);
                            Gson gson=new Gson();
                            LandFragmentBean parse=gson.fromJson(response,LandFragmentBean.class);
                            if ("OK".equals(parse.getStatus())){
                                mLandInfoList.clear();
                                mLandInfoList.addAll(parse.getLaninfo());
                                mLandInfoRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    private void setLandFragment(){
        mLandInfoList=new ArrayList<>();
        mLandInfoRecyclerAdapter=new BaseRecyclerAdapter<LandFragmentBean.LaninfoBean>
                (this,mLandInfoList,R.layout.ly_activity_seed_plant_land_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandFragmentBean.LaninfoBean laninfoBean, int position) {
                holder.setCheckBoxTxt(R.id.seed_plant_rv_land_check_box,laninfoBean.getLandNme()+"/余"+laninfoBean.getUseArea()+"㎡");
                holder.setClick(R.id.seed_plant_rv_land_rl,laninfoBean,position,mLandInfoRecyclerAdapter);
                if (isShowLand){
                    if (showLandPosition==position){
                        holder.setCheck(R.id.seed_plant_rv_land_check_box,true);
                        seed_plant_covered_tv.setText("占地约"+laninfoBean.getLandArea()+"㎡");
                        landId = laninfoBean.getGoodsId();
                    }else {
                        holder.setCheck(R.id.seed_plant_rv_land_check_box,false);
                    }
                }
            }

            @Override
            public void clickEvent(int viewId, LandFragmentBean.LaninfoBean laninfoBean, int position) {
                super.clickEvent(viewId, laninfoBean, position);
                switch (viewId){
                    case R.id.seed_plant_rv_land_rl:
                        isShowLand=true;
                        showLandPosition=position;
                        notifyDataSetChanged();
                        break;
                }
            }
        };
        seed_plant_seed_land_rv.setAdapter(mLandInfoRecyclerAdapter);
    }


    /**
     *  获取须知 -网络请求
     */
    private void initTipSelect(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_TIP_SELECT)
                    .addParams("typeId","Sowing_notes")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取手机提示 = "+response);
                            Gson gson=new Gson();
                            DefaultBean parse=gson.fromJson(response,DefaultBean.class);
                            if ("OK".equals(parse.getStatus())){
                                seed_plant_notes_tv.setText(parse.getMsg());
                            }
                        }
                    });
        }
    }


    /**
     *  获取账户余额   -网络请求
     */
    private void initAccountData(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_ACCOUNT)
                    .addParams("userId", SpSimpleUtils.getSp("userid",this,"LoginActivity"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取账户余额 = " +response);
                            Gson gson=new Gson();
                            DefaultBean parse=gson.fromJson(response,DefaultBean.class);
                            if ("OK".equals(parse.getStatus())){
                                priceCompare=Double.parseDouble(parse.getData());
                            }
                        }
                    });
        }
    }


    /**
     *  创建订单   -网络请求
     */
    private void initOrderCreate(){
        List<Map<String,String>> goodsList=new ArrayList<>();
        for (int i = 0; i <mSeedNewList.size() ; i++) {
            Map<String,String> goodList=new HashMap<>();
            goodList.put("goodsId",mSeedNewList.get(i).getGId());
            goodList.put("unitPrice",mSeedNewList.get(i).getPrice());
            goodList.put("goodsNum",mSeedNewList.get(i).getCount()+"");
            goodsList.add(goodList);
        }
        String s = new Gson().toJson(goodsList);
        LyLog.i(TAG,"这里面的值 = "+s);
        LyLog.i(TAG,"土地id = "+landId);
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_ORDER_CREATE)
                    .addParams("userId", SpSimpleUtils.getSp("userid",SeedPlantActivity.this,"LoginActivity"))
                    .addParams("totalAmt",seed_plant_money_tv.getText().toString())
                    .addParams("payType","ZHYE")
                    .addParams("Remark",seed_plant_remarks_tv.getText().toString())
                    .addParams("landId",landId)
                    .addParams("goodsList",s)
                    .addParams("goodsType","SEED")
                    .addParams("monthNum","")
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
                                LyToast.shortToast(SeedPlantActivity.this,parse.getMsg());
                                startActivity(new Intent(SeedPlantActivity.this,LandPaymentResultsActivity.class)
                                        .putExtra("result","种植结果"));
                                EventBus.getDefault().post(new EventBusDefaultBean("SeedOK"));
                                finish();

                            }else {
                                LyToast.shortToast(SeedPlantActivity.this,parse.getMsg());
                            }
                        }
                    });
        }
    }


    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_seed_plant_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setTxt(R.id.seed_plant_rv_item_name_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.seed_plant_rv_item_money_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
            }


        };
        seed_plant_seed_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean("特级番茄种子x10（10㎡）","100");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("土豆种子x10（20㎡）","150");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("西瓜种子x5（50㎡）","250");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("","");
        mList.add(landDetailsNameBean);
        return mList;
    }
}
