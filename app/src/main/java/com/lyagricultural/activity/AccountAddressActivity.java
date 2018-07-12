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
import com.lyagricultural.utils.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * 作者Administrator on 2018/6/4 0004 10:04
 */
public class AccountAddressActivity extends BaseActivity  implements View.OnClickListener{
    private static final String TAG = "AccountAddressActivity";
    private RecyclerView account_address_rv;
    private RelativeLayout account_address_rl;
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;

    private BaseRecyclerAdapter<AccountAddressBean.AdlistBean> baseAccountAddressRecyclerAdapter;
    private List<AccountAddressBean.AdlistBean> mAccountAddressList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_address);
        setTitle("地址管理");
        initView();
    }

    private void initView(){
        account_address_rv=findViewById(R.id.account_address_rv);
        account_address_rl=findViewById(R.id.account_address_rl);
        account_address_rl.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        account_address_rv.setLayoutManager(layoutManager);
//        setLandRv();
        setAccountAddressRv();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAccountAddress();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_address_rl:
                startActivity(new Intent(AccountAddressActivity.this,AccountAddressAddActivity.class)
                .putExtra("title","新增地址"));
                break;
        }
    }
    /**
     *  获取收货地址   -网络请求
     */
    private void initAccountAddress(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_ADDRESS)
                    .addParams("userId", SpUtils.getSp("userid",AccountAddressActivity.this,"LoginActivity"))
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
                    .addParams("userId", SpUtils.getSp("userid",AccountAddressActivity.this,"LoginActivity"))
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
                                LyToast.shortToast(AccountAddressActivity.this,parse.getMsg());
                               initAccountAddress();
                            }else {
                                LyToast.shortToast(AccountAddressActivity.this,parse.getMsg());
                            }
                        }
                    });
        }
    }


    private void setAccountAddressRv(){
        mAccountAddressList=new ArrayList<>();
        baseAccountAddressRecyclerAdapter=new BaseRecyclerAdapter<AccountAddressBean.AdlistBean>(this,mAccountAddressList,R.layout.ly_activity_account_address_rv_item) {
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
                holder.setClick(R.id.account_address_rv_edit_ll,adlistBean,position,baseAccountAddressRecyclerAdapter);
                holder.setClick(R.id.account_address_rv_address_cb_rl,adlistBean,position,baseAccountAddressRecyclerAdapter);

            }

            @Override
            public void clickEvent(int viewId, final AccountAddressBean.AdlistBean adlistBean, int position) {
                super.clickEvent(viewId, adlistBean, position);
                switch (viewId){
                    case R.id.account_address_rv_address_cb_rl:
                        if ("0".equals(adlistBean.getIsOk())){
                            new CommomDialog(AccountAddressActivity.this, R.style.dialog, "您确定修改此地址为默认地址？", new CommomDialog.OnCloseListener() {
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
                    case R.id.account_address_rv_edit_ll:
                        startActivity(new Intent(AccountAddressActivity.this,AccountAddressModifyActivity.class)
                                .putExtra("title","编辑地址")
                                .putExtra("adId",adlistBean.getAdId())
                                .putExtra("adNme",adlistBean.getAdNme())
                                .putExtra("adPhone",adlistBean.getAdPhone())
                                .putExtra("proNme",adlistBean.getProNme())
                                .putExtra("cityNme",adlistBean.getCityNme())
                                .putExtra("disNme",adlistBean.getDisNme())
                                .putExtra("adDetail",adlistBean.getAdDetail())
                                .putExtra("zipCode",adlistBean.getZipCode())
                                .putExtra("isOk",adlistBean.getIsOk())

                        );

                        break;
                }
            }
        };
        account_address_rv.setAdapter(baseAccountAddressRecyclerAdapter);
    }




    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_account_address_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setTxt(R.id.account_address_rv_name_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.account_address_rv_phone_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
                holder.setTxt(R.id.account_address_rv_address_tv,landDetailsNameBean.getLand_details_name_rv_tv_t());
            }

        };
        account_address_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean("王萌萌","189****4212","重庆市渝中区上清寺中安国际大厦12-6");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("王刚","189****4212","重庆市渝中区上清寺中安国际大厦12-6");
        mList.add(landDetailsNameBean);
        return mList;
    }
}
