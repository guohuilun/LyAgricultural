package com.lyagricultural.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.DefaultBean;
import com.lyagricultural.cebean.CeJsonBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.PhoneUtils;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.utils.addresss.AddressPickTask;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import okhttp3.Call;

/**
 * 作者Administrator on 2018/6/27 0027 14:27
 */
public class AccountAddressModifyActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "AccountAddressModifyActivity";
    private EditText account_address_add_name_et;
    private EditText account_address_add_phone_et;
    private RelativeLayout account_address_add_select_rl;
    private TextView account_address_add_select_et;
    private EditText account_address_add_detailed_et;
    private Button account_address_add_button;
    private String adId;
    private String adNme;
    private String adPhone;
    private String proNme;
    private String cityNme;
    private String disNme;
    private String adDetail;
    private String zipCode;
    private String isOk;
    private CheckBox account_address_modify_address_cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_address_modify);
        initView();
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
            String title = intent.getStringExtra("title");
            setTitle(title);
            adId = intent.getStringExtra("adId");
            adNme = intent.getStringExtra("adNme");
            adPhone = intent.getStringExtra("adPhone");
            proNme=intent.getStringExtra("proNme");
            cityNme=intent.getStringExtra("cityNme");
            disNme=intent.getStringExtra("disNme");
            adDetail = intent.getStringExtra("adDetail");
            zipCode = intent.getStringExtra("zipCode");
            isOk = intent.getStringExtra("isOk");
        }
        account_address_add_name_et=findViewById(R.id.account_address_add_name_et);
        account_address_add_phone_et=findViewById(R.id.account_address_add_phone_et);
        account_address_add_select_rl=findViewById(R.id.account_address_add_select_rl);
        account_address_add_select_et=findViewById(R.id.account_address_add_select_et);
        account_address_add_detailed_et=findViewById(R.id.account_address_add_detailed_et);
        account_address_add_button=findViewById(R.id.account_address_add_button);
        account_address_modify_address_cb=findViewById(R.id.account_address_modify_address_cb);
        account_address_add_select_rl.setOnClickListener(this);
        account_address_add_button.setOnClickListener(this);
        account_address_add_name_et.setText(adNme);
        account_address_add_phone_et.setText(adPhone);
        account_address_add_select_et.setText(proNme+cityNme+disNme);
        account_address_add_detailed_et.setText(adDetail);
        if ("1".equals(isOk)){
            account_address_modify_address_cb.setChecked(true);
        }else {
            account_address_modify_address_cb.setChecked(false);
        }

//        initCe();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_address_add_select_rl:
                onAddressPicker();
                break;
            case R.id.account_address_add_button:
                if (account_address_add_name_et.getText().toString().trim().equals("")){
                    LyToast.shortToast(this,"收货人姓名不能为空");
                    return;
                }
                if (account_address_add_phone_et.getText().toString().trim().equals("")){
                    LyToast.shortToast(this,"收货手机号码不能为空");
                    return;
                }

                if (account_address_add_phone_et.length()!=11){
                    LyToast.shortToast(this,"手机号码尾数不够");
                    return;
                }

                if (PhoneUtils.isMobileNO(account_address_add_phone_et.getText().toString().trim())==false){
                    LyToast.shortToast(this,"请输入正确的手机号");
                    return;
                }

                if (account_address_add_select_et.getText().toString().trim().equals("请选择地区")){
                    LyToast.shortToast(this,"地区不能为空");
                    return;
                }

                if (account_address_add_detailed_et.getText().toString().trim().equals("")){
                    LyToast.shortToast(this,"地址不能为空");
                    return;
                }

                adNme=account_address_add_name_et.getText().toString().trim();
                adPhone=account_address_add_phone_et.getText().toString().trim();
                adDetail=account_address_add_detailed_et.getText().toString().trim();
                if (account_address_modify_address_cb.isChecked()){
                    LyLog.i(TAG,"是否选中1 ");
                    isOk="1";
                }else {
                    LyLog.i(TAG,"是否选中0 ");
                    isOk="0";
                }
                initAccountAddressUpdate(adId,adNme,adPhone,proNme,cityNme,disNme,adDetail,zipCode,isOk);
                break;

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
                    .addParams("userId", SpSimpleUtils.getSp("userid",AccountAddressModifyActivity.this,"LoginActivity"))
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
                                LyToast.shortToast(AccountAddressModifyActivity.this,parse.getMsg());
                                finish();
                            }else {
                                LyToast.shortToast(AccountAddressModifyActivity.this,parse.getMsg());
                            }
                        }
                    });
        }
    }

    /**
     * 获取地址
     */
    public void onAddressPicker() {
        AddressPickTask task = new AddressPickTask(this);
//        task.setHideProvince(true);//隐藏省，显示市、县
//        task.setHideCounty(true);//隐藏县，显示省、市
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                LyToast.shortToast(AccountAddressModifyActivity.this, "数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                String address;
                if (county == null) {
                    address = province.getAreaName() + city.getAreaName();
                    LyToast.shortToast(AccountAddressModifyActivity.this, province.getAreaName() + city.getAreaName());
                } else {
                    address = province.getAreaName() + city.getAreaName() + county.getAreaName();
                    LyToast.shortToast(AccountAddressModifyActivity.this, province.getAreaName() + city.getAreaName() + county.getAreaName());
                }
                proNme=province.getAreaName();
                cityNme=city.getAreaName();
                disNme=county.getAreaName();
                account_address_add_select_et.setText(address);
                account_address_add_select_et.setTextColor(Color.parseColor("#333333"));
            }
        });
        //以下几种格式显示效果相同
//        task.execute("广东省", "深圳市", "龙岗区");//设置默认
//        task.execute("广东", "深圳", "龙岗");//设置默认
//        task.execute("广东省", "深圳", "龙岗");//设置默认
//        task.execute("广", "深圳", "龙岗");//设置默认
//        task.execute("广", "深", "龙岗");//设置默认
        task.execute("广", "深", "龙");//设置默认
    }


    /**
     *  修改收货地址   -网络请求
     */
    private void initCe(){

        List<Map<String,String>> goodsList=new ArrayList<>();
        Map<String,String> goodList=new HashMap<>();
        goodList.put("goodsId","商品Id");
        goodList.put("unitPrice","200");
        goodList.put("goodsNum","200");
        CeJsonBean ceJsonBean=new CeJsonBean();
//        ceJsonBean.userId=SpUtils.getSp("userid",AccountAddressModifyActivity.this,"LoginActivity");
//        ceJsonBean.totalAmt="300";
//        ceJsonBean.payType="微信支付";
//        ceJsonBean.Remark="订单备注";
//        ceJsonBean.landId="土地Id";
        goodsList.add(goodList);
        goodsList.add(goodList);
//        ceJsonBean.goodsList.add(goodList);
        String s = new Gson().toJson(goodsList);
        LyLog.i(TAG,"这里面的值 = "+s);
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_ORDER_CREATE)
                    .addParams("userId", SpSimpleUtils.getSp("userid",AccountAddressModifyActivity.this,"LoginActivity"))
                    .addParams("totalAmt","300")
                    .addParams("payType","微信支付")
                    .addParams("Remark","订单备注")
                    .addParams("landId","土地Id")
                    .addParams("goodsList",s)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"修改 = " +response);
                            Gson gson=new Gson();
                            DefaultBean parse=gson.fromJson(response,DefaultBean.class);
                            if ("OK".equals(parse.getStatus())){
                                LyToast.shortToast(AccountAddressModifyActivity.this,parse.getMsg());
                                finish();
                            }else {
                                LyToast.shortToast(AccountAddressModifyActivity.this,parse.getMsg());
                            }
                        }
                    });
        }
    }

}
