package com.lyagricultural.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.AccountAddressBean;
import com.lyagricultural.bean.DefaultBean;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.dialog.CommomDialog;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.SpSimpleUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * 作者Administrator on 2018/6/4 0004 10:04
 */
public class LandSelectAddressActivity extends BaseActivity {
    private static final String TAG = "LandSelectAddressActivity";
    private RecyclerView account_address_rv;

    private BaseRecyclerAdapter<AccountAddressBean.AdlistBean> baseAccountAddressRecyclerAdapter;
    private List<AccountAddressBean.AdlistBean> mAccountAddressList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_select_address);
        setTitle("选择地址");
        initView();
    }

    private void initView(){
        account_address_rv=findViewById(R.id.account_address_rv);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        account_address_rv.setLayoutManager(layoutManager);
        initAccountAddress();
        setAccountAddressRv();
    }


    /**
     *  获取收货地址   -网络请求
     */
    private void initAccountAddress(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_ADDRESS)
                    .addParams("userId", SpSimpleUtils.getSp("userid",LandSelectAddressActivity.this,"LoginActivity"))
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
                                mAccountAddressList.clear();
                                mAccountAddressList.addAll(parse.getAdlist());
                                baseAccountAddressRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }


    /**
     *  修改收货地址   -网络请求
     */
    private void initAccountAddressUpdate(String adId,String adNme,String adPhone,String proNme
            ,String cityNme,String disNme,String adDetail,String zipCode,String isOk){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_ADDRESS_UPDATE)
                    .addParams("adId",adId)
                    .addParams("userId", SpSimpleUtils.getSp("userid",LandSelectAddressActivity.this,"LoginActivity"))
                    .addParams("adNme",adNme)
                    .addParams("adPhone",adPhone)
                    .addParams("proNme",proNme)
                    .addParams("cityNme",cityNme)
                    .addParams("disNme",disNme)
                    .addParams("adDetail",adDetail)
                    .addParams("zipCode",zipCode)
                    .addParams("isOk",isOk)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"修改收货地址 = " +response);
                            Gson gson=new Gson();
                            DefaultBean parse=gson.fromJson(response,DefaultBean.class);
                            if ("OK".equals(parse.getStatus())){
                                LyToast.shortToast(LandSelectAddressActivity.this,parse.getMsg());
                               initAccountAddress();
                            }else {
                                LyToast.shortToast(LandSelectAddressActivity.this,parse.getMsg());
                            }
                        }
                    });
        }
    }


    private void setAccountAddressRv(){
        mAccountAddressList=new ArrayList<>();
        baseAccountAddressRecyclerAdapter=new BaseRecyclerAdapter<AccountAddressBean.AdlistBean>(this,mAccountAddressList,R.layout.ly_activity_land_select_address_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, AccountAddressBean.AdlistBean adlistBean, int position) {
                holder.setTxt(R.id.account_address_rv_name_tv,adlistBean.getAdNme());
                holder.setTxt(R.id.account_address_rv_phone_tv,adlistBean.getAdPhone());
                holder.setTxt(R.id.account_address_rv_address_tv,adlistBean.getProNme()
                        +adlistBean.getCityNme()
                        +adlistBean.getDisNme()
                        +adlistBean.getAdDetail());


                if ("0".equals(adlistBean.getIsOk())){
                    holder.setCheck(R.id.account_address_rv_address_cb,false);
                }else {
                    holder.setCheck(R.id.account_address_rv_address_cb,true);
                }
                holder.setClick(R.id.account_address_rv_address_cb_rl,adlistBean,position,baseAccountAddressRecyclerAdapter);

            }

            @Override
            public void clickEvent(int viewId, final AccountAddressBean.AdlistBean adlistBean, int position) {
                super.clickEvent(viewId, adlistBean, position);
                switch (viewId){
                    case R.id.account_address_rv_address_cb_rl:
                        if ("0".equals(adlistBean.getIsOk())){
                            new CommomDialog(LandSelectAddressActivity.this, R.style.dialog, "您确定修改此地址为默认地址？", new CommomDialog.OnCloseListener() {
                                @Override
                                public void onClick(Dialog dialog, boolean confirm) {
                                    if (confirm){
                                        adlistBean.setIsOk("1");
                                        initAccountAddressUpdate(adlistBean.getAdId()
                                                ,adlistBean.getAdNme()
                                                ,adlistBean.getAdPhone()
                                                ,adlistBean.getProNme()
                                                ,adlistBean.getCityNme()
                                                ,adlistBean.getDisNme()
                                                ,adlistBean.getAdDetail()
                                                ,adlistBean.getZipCode()
                                                ,adlistBean.getIsOk());
                                        dialog.dismiss();
                                    }
                                }
                            }).setTitle("提示").show();
                        }
                        break;
                }
            }
        };
        account_address_rv.setAdapter(baseAccountAddressRecyclerAdapter);
    }

}
