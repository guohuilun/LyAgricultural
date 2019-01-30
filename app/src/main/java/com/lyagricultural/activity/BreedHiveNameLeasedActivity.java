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
import com.lyagricultural.cebean.LandDetailsNameBean;
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
public class BreedHiveNameLeasedActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "BreedHiveNameLeasedActivity";
    private RelativeLayout breed_hive_name_leased_rl;
    private TextView breed_hive_name_leased_name_display_tv;
    private TextView breed_hive_name_leased_notes_tv;
    private RecyclerView breed_hive_name_leased_rv;
    private TextView breed_hive_name_leased_money_tv;
    private CheckBox breed_hive_name_leased_check_box;
    private Button breed_hive_name_leased_place_order_button;
    private RelativeLayout breed_hive_name_leased_buy_rl;

    private String title="";

    private  BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private  List<LandDetailsNameBean> mList;

    private TextView breed_hive_name_leased_buy_tv;
    private double priceCompare;
    private double priceCommit;
    private int isCheckShowPosition=0;
    private Boolean isCheckShow=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_breed_hive_name_leased);
        setTitle("蜂箱认领");
        initView();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private void  initView(){
        Intent intent=getIntent();
        if (intent!=null){
            title = intent.getStringExtra("text");
        }
        breed_hive_name_leased_rl=findViewById(R.id.breed_hive_name_leased_rl);
//        breed_hive_name_leased_rl.setVisibility(View.GONE);
        breed_hive_name_leased_name_display_tv=findViewById(R.id.breed_hive_name_leased_name_display_tv);
        breed_hive_name_leased_notes_tv=findViewById(R.id.breed_hive_name_leased_notes_tv);
        breed_hive_name_leased_rv=findViewById(R.id.breed_hive_name_leased_rv);
        breed_hive_name_leased_buy_tv=findViewById(R.id.breed_hive_name_leased_buy_tv);
        breed_hive_name_leased_money_tv=findViewById(R.id.breed_hive_name_leased_money_tv);
        breed_hive_name_leased_check_box=findViewById(R.id.breed_hive_name_leased_check_box);
        breed_hive_name_leased_place_order_button=findViewById(R.id.breed_hive_name_leased_place_order_button);
        breed_hive_name_leased_buy_rl=findViewById(R.id.breed_hive_name_leased_buy_rl);
        breed_hive_name_leased_place_order_button.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(BreedHiveNameLeasedActivity.this,3);
        breed_hive_name_leased_rv.setLayoutManager(layoutManager);
        breed_hive_name_leased_name_display_tv.setText(title);
        breed_hive_name_leased_money_tv.setText("1000");
        SpannableStringBuilder spannableMoney = new SpannableStringBuilder("货币不足，请充值");
        spannableMoney.setSpan(new TextSpan(){
            @Override
            public void onClick(View view) {
               startActivity(new Intent(BreedHiveNameLeasedActivity.this,AccountRechargeActivity.class));
            }
        },6,8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        breed_hive_name_leased_buy_tv.setText(spannableMoney);
        breed_hive_name_leased_buy_tv.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        breed_hive_name_leased_buy_rl.setVisibility(View.INVISIBLE);

        setLandRv();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.breed_hive_name_leased_place_order_button:

                if (priceCommit>priceCompare){
                   LyToast.shortToast(this,"当前余额不足，请充值");
                    return;
                }

               /* if (!breed_hive_name_leased_check_box.isChecked()){
                    LyToast.shortToast(this,"请阅读并同意租赁须知");
                    return;
                }*/

                startActivity(new Intent(BreedHiveNameLeasedActivity.this,BreedHiveNamePlaceOrderActivity.class)
                .putExtra("text",title)
                .putExtra("priceCommit",priceCommit)
                .putExtra("priceCompare",priceCompare)
                );

                break;
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
                                priceCommit=Double.parseDouble(breed_hive_name_leased_money_tv.getText().toString().trim());
                                LyLog.i(TAG,"这里面的数值0 = "+priceCompare+"  "+priceCommit);
                                if (priceCommit>priceCompare){
                                    breed_hive_name_leased_buy_rl.setVisibility(View.VISIBLE);
                                }else {
                                    breed_hive_name_leased_buy_rl.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
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

    private void setDouble(){
        priceCommit=Double.parseDouble(breed_hive_name_leased_money_tv.getText().toString().trim());

        if (priceCommit>priceCompare){
            LyLog.i(TAG,"这里面的数值1 = "+priceCompare+"  "+priceCommit);
            breed_hive_name_leased_buy_rl.setVisibility(View.VISIBLE);
        }else {
            LyLog.i(TAG,"这里面的数值2 = "+priceCompare+"  "+priceCommit);
            breed_hive_name_leased_buy_rl.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_land_leased_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
               holder.setTxt(R.id.land_leased_rv_item_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.land_leased_money_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
                if (isCheckShowPosition==position){
                    if (isCheckShow){
                        holder.setCheck(R.id.land_leased_rv_item_check_box,true);
                        breed_hive_name_leased_money_tv.setText(landDetailsNameBean.getLand_details_name_rv_tv_o());
                        setDouble();
                    }else {
                        if (position==0){
                            holder.setCheck(R.id.land_leased_rv_item_check_box,true);
                            breed_hive_name_leased_money_tv.setText(landDetailsNameBean.getLand_details_name_rv_tv_o());
                        }
                    }
                }else {
                    holder.setCheck(R.id.land_leased_rv_item_check_box,false);
                }

                holder.setClick(R.id.land_leased_rv_item_rl,landDetailsNameBean,position,baseRecyclerAdapter);
            }

            @Override
            public void clickEvent(int viewId, LandDetailsNameBean landDetailsNameBean, int position) {
                super.clickEvent(viewId, landDetailsNameBean, position);
                switch (viewId){
                    case R.id.land_leased_rv_item_rl:
                        isCheckShow=true;
                        isCheckShowPosition=position;
                        notifyDataSetChanged();
                        break;
                }
            }
        };
        breed_hive_name_leased_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean("半年（180天）","1000",false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("1年（365天）","2000",false);
        mList.add(landDetailsNameBean);

        return mList;
    }

}
