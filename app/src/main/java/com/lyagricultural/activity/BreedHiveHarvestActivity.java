package com.lyagricultural.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.AccountAddressBean;
import com.lyagricultural.bean.EventBusBreedHiveBean;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.view.amount.AmountView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/7/27 0027 09:17
 */
public class BreedHiveHarvestActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "BreedHiveHarvestActivity";
    private RelativeLayout breed_hive_harvest_address_rl;
    private LinearLayout breed_hive_harvest_content_ll;
    private TextView breed_hive_harvest_content_name_tv;
    private TextView breed_hive_harvest_content_phone_tv;
    private TextView breed_hive_harvest_content_address_tv;
    private TextView breed_hive_harvest_add_tv;
    private RecyclerView breed_hive_harvest_rv;
    private TextView breed_hive_harvest_all_num_tv;
    private Button breed_hive_harvest_confirm_button;

    private String state;
    private String seedOut="";

    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_breed_hive_harvest);
        setTitle("收获");
        initView();
    }

    private void initView(){
        breed_hive_harvest_address_rl=findViewById(R.id.breed_hive_harvest_address_rl);
        breed_hive_harvest_content_ll=findViewById(R.id.breed_hive_harvest_content_ll);
        breed_hive_harvest_content_name_tv=findViewById(R.id.breed_hive_harvest_content_name_tv);
        breed_hive_harvest_content_phone_tv=findViewById(R.id.breed_hive_harvest_content_phone_tv);
        breed_hive_harvest_content_address_tv=findViewById(R.id.breed_hive_harvest_content_address_tv);
        breed_hive_harvest_add_tv=findViewById(R.id.breed_hive_harvest_add_tv);
        breed_hive_harvest_rv=findViewById(R.id.breed_hive_harvest_rv);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        breed_hive_harvest_rv.setLayoutManager(layoutManager);
        breed_hive_harvest_all_num_tv=findViewById(R.id.breed_hive_harvest_all_num_tv);
        breed_hive_harvest_confirm_button=findViewById(R.id.breed_hive_harvest_confirm_button);
        breed_hive_harvest_address_rl.setOnClickListener(this);
        breed_hive_harvest_confirm_button.setOnClickListener(this);
        breed_hive_harvest_all_num_tv.setText("0");
        setLandRv();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.breed_hive_harvest_address_rl:
                if ("OK".equals(state)){
                    startActivity(new Intent(BreedHiveHarvestActivity.this,LandSelectAddressActivity.class));
                }else {
                    startActivity(new Intent(BreedHiveHarvestActivity.this,AccountAddressActivity.class));
                }
                break;
            case R.id.breed_hive_harvest_confirm_button:
                startActivity(new Intent(this,BreedHiveHarvestSuccessActivity.class));
                seedOut="seedOut";
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAccountAddress();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ("".equals(seedOut)){
            EventBus.getDefault().post(new EventBusBreedHiveBean("OK"));
            LyLog.i(TAG," OK ");
        }else if ("seedOut".equals(seedOut)){
            LyLog.i(TAG," seedOut ");
            EventBus.getDefault().post(new EventBusBreedHiveBean("seedOut"));
        }

    }

    /**
     *  获取收货地址   -网络请求
     */
    private void initAccountAddress(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_ADDRESS)
                    .addParams("userId", SpSimpleUtils.getSp("userid",BreedHiveHarvestActivity.this,"LoginActivity"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取收货地址 = " +response);
                            Gson gson=new Gson();
                            AccountAddressBean parse=gson.fromJson(response,AccountAddressBean.class);
                            if ("OK".equals(parse.getStatus())){
                                state=parse.getStatus();

                                for (int i = 0; i <parse.getAdlist().size() ; i++) {
                                    if ("1".equals(parse.getAdlist().get(i).getIsOk())) {
                                        breed_hive_harvest_content_name_tv.setText("收货人："+parse.getAdlist().get(i).getAdNme());
                                        breed_hive_harvest_content_phone_tv.setText(parse.getAdlist().get(i).getAdPhone());
                                        breed_hive_harvest_content_address_tv.setText("收货地址："+parse.getAdlist().get(i).getProNme()
                                                +parse.getAdlist().get(i).getCityNme()
                                                +parse.getAdlist().get(i).getDisNme()
                                                +parse.getAdlist().get(i).getAdDetail()
                                        );
                                        breed_hive_harvest_content_ll.setVisibility(View.VISIBLE);
                                        breed_hive_harvest_add_tv.setVisibility(View.GONE);
                                        break;
                                    }else {
                                        breed_hive_harvest_content_ll.setVisibility(View.GONE);
                                        breed_hive_harvest_add_tv.setVisibility(View.VISIBLE);
                                        breed_hive_harvest_add_tv.setText("请选择地址设为默认地址");
                                    }
                                }
                            }else {
                                breed_hive_harvest_content_ll.setVisibility(View.GONE);
                                breed_hive_harvest_add_tv.setVisibility(View.VISIBLE);
                                state=parse.getStatus();
                            }
                        }
                    });
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



    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_breed_hive_harvest_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landFragmentBean, int position) {
                holder.setTxt(R.id.breed_hive_harvest_rv_tv,landFragmentBean.getLand_details_name_rv_tv());
                holder.setAmountEditViewAllTxt(R.id.breed_hive_harvest_av,20);
                holder.setAmountEditViewListener(R.id.breed_hive_harvest_av, new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        breed_hive_harvest_all_num_tv.setText(String.valueOf(amount));
                    }
                });
            }
        };
        breed_hive_harvest_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landFragmentBean=new LandDetailsNameBean
                ("蜂蜜(20)","渝北区");
        mList.add(landFragmentBean);
        return mList;
    }

}
