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
import com.lyagricultural.bean.DefaultBean;
import com.lyagricultural.bean.EventBusLandDetailsBean;
import com.lyagricultural.bean.LandDetailsNameSelectBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.view.amount.AmountView;
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
 * 作者Administrator on 2018/7/27 0027 09:17
 */
public class LandHarvestActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "LandHarvestActivity";
    private RelativeLayout hand_harvest_address_rl;
    private LinearLayout hand_harvest_content_ll;
    private TextView hand_harvest_content_name_tv;
    private TextView hand_harvest_content_phone_tv;
    private TextView hand_harvest_content_address_tv;
    private TextView hand_harvest_add_tv;
    private RecyclerView hand_harvest_rv;
    private TextView land_harvest_crop_type_num_tv;
    private TextView land_harvest_all_num_tv;
    private Button hand_harvest_confirm_button;

    private String state;
    private String seedOut="";

    private List<LandDetailsNameSelectBean.CropinfoBean> mCropInfoBeanList=new ArrayList<>();
    private BaseRecyclerAdapter<LandDetailsNameSelectBean.CropinfoBean> mCropInfoListAdapter;
    private int allCount=0;

    private String adId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_harvest);
        setTitle("收获");
        initView();
          if (!EventBus.getDefault().isRegistered(this)){
            // 以Sticky的形式注册
            EventBus.getDefault().register(this);
        }
    }

    private void initView(){
        hand_harvest_address_rl=findViewById(R.id.hand_harvest_address_rl);
        hand_harvest_content_ll=findViewById(R.id.hand_harvest_content_ll);
        hand_harvest_content_name_tv=findViewById(R.id.hand_harvest_content_name_tv);
        hand_harvest_content_phone_tv=findViewById(R.id.hand_harvest_content_phone_tv);
        hand_harvest_content_address_tv=findViewById(R.id.hand_harvest_content_address_tv);
        hand_harvest_add_tv=findViewById(R.id.hand_harvest_add_tv);
        hand_harvest_rv=findViewById(R.id.hand_harvest_rv);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        hand_harvest_rv.setLayoutManager(layoutManager);
        land_harvest_crop_type_num_tv=findViewById(R.id.land_harvest_crop_type_num_tv);
        land_harvest_all_num_tv=findViewById(R.id.land_harvest_all_num_tv);
        hand_harvest_confirm_button=findViewById(R.id.hand_harvest_confirm_button);
        hand_harvest_address_rl.setOnClickListener(this);
        hand_harvest_confirm_button.setOnClickListener(this);
        setmCropInfoListAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.hand_harvest_address_rl:
                if ("OK".equals(state)){
                    startActivity(new Intent(LandHarvestActivity.this,LandSelectAddressActivity.class));
                }else {
                    startActivity(new Intent(LandHarvestActivity.this,AccountAddressActivity.class));
                }
                break;
            case R.id.hand_harvest_confirm_button:
                initHarvest();
//                startActivity(new Intent(LandHarvestActivity.this,LandHarvestSuccessActivity.class));
//                seedOut="seedOut";
//                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAccountAddress();
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void sentParsms(List<LandDetailsNameSelectBean.CropinfoBean> mCropInfoEventBeanList) {//此方法类似于广播，任何地方都可以传递
        mCropInfoBeanList.clear();
        mCropInfoBeanList.addAll(mCropInfoEventBeanList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ("".equals(seedOut)){
            EventBus.getDefault().post(new EventBusLandDetailsBean("OK"));
            LyLog.i(TAG," OK ");
        }else if ("seedOut".equals(seedOut)){
            LyLog.i(TAG," seedOut ");
            EventBus.getDefault().post(new EventBusLandDetailsBean("seedOut"));
        }
            if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     *  获取收货地址   -网络请求
     */
    private void initAccountAddress(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_ADDRESS)
                    .addParams("userId", SpSimpleUtils.getSp("userid",LandHarvestActivity.this,"LoginActivity"))
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
                                        hand_harvest_content_name_tv.setText("收货人："+parse.getAdlist().get(i).getAdNme());
                                        hand_harvest_content_phone_tv.setText(parse.getAdlist().get(i).getAdPhone());
                                        hand_harvest_content_address_tv.setText("收货地址："+parse.getAdlist().get(i).getProNme()
                                                +parse.getAdlist().get(i).getCityNme()
                                                +parse.getAdlist().get(i).getDisNme()
                                                +parse.getAdlist().get(i).getAdDetail()
                                        );
                                        hand_harvest_content_ll.setVisibility(View.VISIBLE);
                                        hand_harvest_add_tv.setVisibility(View.GONE);
                                        adId = parse.getAdlist().get(i).getAdId();
                                        break;
                                    }else {
                                        hand_harvest_content_ll.setVisibility(View.GONE);
                                        hand_harvest_add_tv.setVisibility(View.VISIBLE);
                                        hand_harvest_add_tv.setText("请选择地址设为默认地址");
                                    }
                                }
                            }else {
                                hand_harvest_content_ll.setVisibility(View.GONE);
                                hand_harvest_add_tv.setVisibility(View.VISIBLE);
                                state=parse.getStatus();
                            }
                        }
                    });
        }
    }


    private void setmCropInfoListAdapter(){
        mCropInfoListAdapter=new BaseRecyclerAdapter<LandDetailsNameSelectBean.CropinfoBean>
                (this,mCropInfoBeanList,R.layout.ly_activity_land_harvest_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, final LandDetailsNameSelectBean.CropinfoBean cropinfoBean, int position) {
                holder.setImg(LandHarvestActivity.this,cropinfoBean.getCropImg(),R.id.land_harvest_rv_iv);
                holder.setTxt(R.id.land_harvest_rv_tv,cropinfoBean.getCropNme()+"("+cropinfoBean.getCropNum()+")");
                cropinfoBean.setShowNum(1);
                setAllCount();
                int cropNum=Integer.valueOf(cropinfoBean.getCropNum());
                holder.setAmountEditViewAllTxt(R.id.land_harvest_av,cropNum);
                holder.setAmountEditViewListener(R.id.land_harvest_av, new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        cropinfoBean.setShowNum(amount);
                        setAllCount();
                    }
                });
            }
        };
        hand_harvest_rv.setAdapter(mCropInfoListAdapter);
        mCropInfoListAdapter.notifyDataSetChanged();
    }

    private void setAllCount(){
        allCount=0;
        land_harvest_crop_type_num_tv.setText(String.valueOf(mCropInfoBeanList.size()));
        for (int i = 0; i <mCropInfoBeanList.size() ; i++) {
            allCount+=mCropInfoBeanList.get(i).getShowNum();
        }
        LyLog.i(TAG,"总数量 = "+allCount);
        land_harvest_all_num_tv.setText(String.valueOf(allCount));
    }


    /**
     *  确认收获   -网络请求
     */
    private void initHarvest(){
        List<Map<String,String>> goodsList=new ArrayList<>();
        for (int i = 0; i <mCropInfoBeanList.size() ; i++) {
            if (mCropInfoBeanList.get(i).getShowNum()>0){
                Map<String,String> goodList=new HashMap<>();
                goodList.put("goodsNme",mCropInfoBeanList.get(i).getCropNme());
                goodList.put("goodsNum",mCropInfoBeanList.get(i).getShowNum()+"");
                goodList.put("cropId",mCropInfoBeanList.get(i).getCropId());
                goodsList.add(goodList);
            }
        }
        String s = new Gson().toJson(goodsList);
        LyLog.i(TAG,"上传的值 = "+s);

        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_EX_CREATE)
                    .addParams("userId", SpSimpleUtils.getSp("userid",LandHarvestActivity.this,"LoginActivity"))
                    .addParams("exNme","土地采摘")
                    .addParams("addressId",adId)
                    .addParams("goodsList",s)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"确认收获 = " +response);
                            Gson gson=new Gson();
                            DefaultBean parse=gson.fromJson(response,DefaultBean.class);
                            if ("OK".equals(parse.getStatus())){
                                LyToast.shortToast(LandHarvestActivity.this,parse.getMsg());
                                startActivity(new Intent(LandHarvestActivity.this,LandHarvestSuccessActivity.class));
                                seedOut="seedOut";
                                finish();
                            }else {
                                LyToast.shortToast(LandHarvestActivity.this,parse.getMsg());
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

}
