package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.DefaultBean;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.bean.LandLeasedGoodsPriceBean;
import com.lyagricultural.cebean.LandFragmentBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
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
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/5/30 0030 15:17
 */
public class LandLeasedActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "LandLeasedActivity";
    private RelativeLayout land_leased_rl;
    private TextView land_leased_name_display_tv;
    private TextView land_leased_size_tv;
    private TextView land_leased_notes_tv;
    private RecyclerView land_leased_rv;
    private TextView land_leased_money_tv;
    private CheckBox land_leased_check_box;
    private Button land_leased_place_order_button;
    private RelativeLayout land_leased_buy_rl;

    private  BaseRecyclerAdapter<LandFragmentBean> baseRecyclerAdapter;
    private  List<LandFragmentBean> mList;

    private  BaseRecyclerAdapter<LandLeasedGoodsPriceBean.PricelistBean> priceListBeanBaseRecyclerAdapter;
    private  List<LandLeasedGoodsPriceBean.PricelistBean> mGoodsList;

    private String nme;
    private String area;
    private String price;
    private String gid;

    private int isCheckShowPosition=0;
    private Boolean isCheckShow=false;
    private TextView land_leased_buy_tv;
    private double priceCompare;
    private String monthNum="";
    private double priceCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_leased);
        setTitle("租赁土地");
        initView();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private void  initView(){
        Intent intent=getIntent();
        if (intent!=null){
            nme=intent.getStringExtra("nme");
            area=intent.getStringExtra("area");
            price = intent.getStringExtra("price");
            gid=intent.getStringExtra("gid");
        }
        land_leased_rl=findViewById(R.id.land_leased_rl);
//        land_leased_rl.setVisibility(View.GONE);
        land_leased_name_display_tv=findViewById(R.id.land_leased_name_display_tv);
        land_leased_size_tv=findViewById(R.id.land_leased_size_tv);
        land_leased_notes_tv=findViewById(R.id.land_leased_notes_tv);
        land_leased_rv=findViewById(R.id.land_leased_rv);
        land_leased_buy_tv=findViewById(R.id.land_leased_buy_tv);
        land_leased_money_tv=findViewById(R.id.land_leased_money_tv);
        land_leased_check_box=findViewById(R.id.land_leased_check_box);
        land_leased_place_order_button=findViewById(R.id.land_leased_place_order_button);
        land_leased_buy_rl=findViewById(R.id.land_leased_buy_rl);
        land_leased_place_order_button.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(LandLeasedActivity.this,3);
        land_leased_rv.setLayoutManager(layoutManager);
        land_leased_name_display_tv.setText(nme);
        land_leased_size_tv.setText(area+"㎡");
        land_leased_money_tv.setText(price);
        SpannableStringBuilder spannableMoney = new SpannableStringBuilder("货币不足，请充值");
        spannableMoney.setSpan(new TextSpan(){
            @Override
            public void onClick(View view) {
               startActivity(new Intent(LandLeasedActivity.this,AccountRechargeActivity.class));
            }
        },6,8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        land_leased_buy_tv.setText(spannableMoney);
        land_leased_buy_tv.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        land_leased_buy_rl.setVisibility(View.INVISIBLE);

        initLandLeasedGoodsPrice();
        initLandLeasedTipSelect();
//        setLandRv();
        setLandLeasedGoodRv();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.land_leased_place_order_button:

                if (priceCommit>priceCompare){
                   LyToast.shortToast(this,"当前余额不足，请充值");
                    return;
                }

                if (!land_leased_check_box.isChecked()){
                    LyToast.shortToast(this,"请阅读并同意租赁须知");
                    return;
                }

                startActivity(new Intent(LandLeasedActivity.this,LandPlaceOrderActivity.class)
                .putExtra("monthNum",monthNum)
                .putExtra("priceCommit",priceCommit)
                .putExtra("nme",nme)
                .putExtra("gid",gid)
                .putExtra("priceCompare",priceCompare)
                );
                break;
        }
    }

    /**
     *  获取土地价格 -网络请求
     */
    private void initLandLeasedGoodsPrice(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.GOODS_PRICE)
                    .addParams("GoodsId",gid)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取土地价格 = "+response);
                            Gson gson=new Gson();
                            LandLeasedGoodsPriceBean parse=gson.fromJson(response,LandLeasedGoodsPriceBean.class);
                            if ("OK".equals(parse.getStatus())){
                                mGoodsList.clear();
                                mGoodsList.addAll(parse.getPricelist());
                                priceListBeanBaseRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    private void setLandLeasedGoodRv(){
        mGoodsList=new ArrayList<>();
        priceListBeanBaseRecyclerAdapter=new BaseRecyclerAdapter<LandLeasedGoodsPriceBean.PricelistBean>(this,mGoodsList,R.layout.ly_activity_land_leased_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandLeasedGoodsPriceBean.PricelistBean pricelistBean, int position) {
                int width = getWindowManager().getDefaultDisplay().getWidth();
                holder.setRelWidth(R.id.land_leased_rv_item_rl,width/3);
                holder.setTxt(R.id.land_leased_rv_item_tv,pricelistBean.getNme());
                holder.setTxt(R.id.land_leased_money_tv,pricelistBean.getPrice());
                holder.setClick(R.id.land_leased_rv_item_rl,pricelistBean,position,priceListBeanBaseRecyclerAdapter);

                if (isCheckShowPosition==position){
                    if (isCheckShow){
                        /*if (pricelistBean.getShow()){
                            holder.setCheck(R.id.land_leased_rv_item_check_box,true);
                            land_leased_money_tv.setText(pricelistBean.getPrice());
                            monthNum=pricelistBean.getMonthNum();
                            setDouble();
                            pricelistBean.setShow(true);
                        } else {
                            holder.setCheck(R.id.land_leased_rv_item_check_box,false);
                            land_leased_money_tv.setText(price);
                            setDouble();
                            pricelistBean.setShow(false);
                        }*/
                        holder.setCheck(R.id.land_leased_rv_item_check_box,true);
                        land_leased_money_tv.setText(pricelistBean.getPrice());
                        monthNum=pricelistBean.getMonthNum();
                        setDouble();

                    }else {
                        if (position==0){
                            holder.setCheck(R.id.land_leased_rv_item_check_box,true);
                            land_leased_money_tv.setText(pricelistBean.getPrice());
                            monthNum=pricelistBean.getMonthNum();
                        }
                    }
                }else {
                    holder.setCheck(R.id.land_leased_rv_item_check_box,false);
                    pricelistBean.setShow(false);
                }

            }

            @Override
            public void clickEvent(int viewId, LandLeasedGoodsPriceBean.PricelistBean pricelistBean, int position) {
                super.clickEvent(viewId, pricelistBean, position);
                switch (viewId){
                    case R.id.land_leased_rv_item_rl:
//                        pricelistBean.setShow(!pricelistBean.getShow());
                        isCheckShow=true;
                        isCheckShowPosition=position;
                        notifyDataSetChanged();
                        break;
                }
            }
        };

        land_leased_rv.setAdapter(priceListBeanBaseRecyclerAdapter);
    }

    /**
     *  获取须知 -网络请求
     */
    private void initLandLeasedTipSelect(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_TIP_SELECT)
                    .addParams("typeId","Tenancy_notes")
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
                                land_leased_notes_tv.setText(parse.getMsg());
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
                                priceCommit=Double.parseDouble(land_leased_money_tv.getText().toString().trim());
                                LyLog.i(TAG,"这里面的数值0 = "+priceCompare+"  "+priceCommit);
                                if (priceCommit>priceCompare){
                                    land_leased_buy_rl.setVisibility(View.VISIBLE);
                                }else {
                                    land_leased_buy_rl.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
        }
    }

    private void setDouble(){
        priceCommit=Double.parseDouble(land_leased_money_tv.getText().toString().trim());

        if (priceCommit>priceCompare){
            LyLog.i(TAG,"这里面的数值1 = "+priceCompare+"  "+priceCommit);
            land_leased_buy_rl.setVisibility(View.VISIBLE);
        }else {
            LyLog.i(TAG,"这里面的数值2 = "+priceCompare+"  "+priceCommit);
            land_leased_buy_rl.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAccountData();
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
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }


    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandFragmentBean>(this,getLandRv(),R.layout.ly_activity_land_leased_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandFragmentBean landFragmentBean, int position) {
            }
        };
        land_leased_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandFragmentBean> getLandRv(){
        mList=new ArrayList<>();
        LandFragmentBean landFragmentBean=new LandFragmentBean();
        mList.add(landFragmentBean);
        return mList;
    }

}
