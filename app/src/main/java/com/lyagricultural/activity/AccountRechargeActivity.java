package com.lyagricultural.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.AccountRechargeNumBean;
import com.lyagricultural.bean.AccountRechargePayBean;
import com.lyagricultural.bean.DefaultBean;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.dialog.CommomDialog;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.weixin.PayEntity;
import com.lyagricultural.weixin.PayUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/6/5 0005 11:20
 */
public class AccountRechargeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AccountRechargeActivity";
    private RecyclerView account_recharge_rv;
    private RelativeLayout account_recharge_mode_rl;
    private CheckBox account_recharge_mode_cb;
    private Button account_recharge_button;
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;
    private Boolean isShowColor=false;
    private int showPos;
    private BaseRecyclerAdapter<AccountRechargeNumBean.NumlistBean> baseAccountRecyclerAdapter;
    private List<AccountRechargeNumBean.NumlistBean> mAccountList;
    private String reNum="";
    private String orderNo;
    private String isSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_recharge);
        setTitle("充值");
        initView();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private void initView(){
        account_recharge_rv=findViewById(R.id.account_recharge_rv);
        account_recharge_mode_rl=findViewById(R.id.account_recharge_mode_rl);
        account_recharge_mode_cb=findViewById(R.id.account_recharge_mode_cb);
        account_recharge_button=findViewById(R.id.account_recharge_button);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,3);
        account_recharge_rv.setLayoutManager(layoutManager);
        account_recharge_mode_rl.setOnClickListener(this);
        account_recharge_button.setOnClickListener(this);
        initAccountRechargeNum();
//        setLandRv();
        setAccountRechargeRv();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_recharge_mode_rl:
                if (account_recharge_mode_cb.isChecked()){
                    account_recharge_mode_cb.setChecked(false);
                }else {
                    account_recharge_mode_cb.setChecked(true);
                }
                break;
            case R.id.account_recharge_button:
                if (reNum.equals("")){
                    LyToast.shortToast(AccountRechargeActivity.this,"请选择充值金额");
                    return;
                }

                if (!account_recharge_mode_cb.isChecked()){
                    LyToast.shortToast(AccountRechargeActivity.this,"请选择充值方式");
                    return;
                }

                initAccountRechargePay();
                break;
        }
    }

    /**
     *  账户充值 -网络请求
     */
    private void initAccountRechargePay(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_RECHARGE)
                    .addParams("userId", SpSimpleUtils.getSp("userid",AccountRechargeActivity.this,"LoginActivity"))
                    .addParams("paymentMode","WX")
                    .addParams("payAmt",reNum)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"账户充值 = "+response);
                            Gson gson=new Gson();
                            AccountRechargePayBean parse=gson.fromJson(response,AccountRechargePayBean.class);
                            if ("OK".equals(parse.getLyStatus())){
                                orderNo=parse.getPayNo();
                                PayUtils payUtils=new PayUtils(AccountRechargeActivity.this);
                                payUtils.wxPay(parse.getLyData());
                             }
                        }
                    });
        }
    }


    /**
     *  账户充值数值 -网络请求
     */
    private void initAccountRechargeNum(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_RECHARGE_NUM)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"账户充值数值 = "+response);
                            Gson gson=new Gson();
                            AccountRechargeNumBean parse=gson.fromJson(response,AccountRechargeNumBean.class);
                            if ("OK".equals(parse.getStatus())){
                              mAccountList.clear();
                              mAccountList.addAll(parse.getNumlist());
                              baseAccountRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    private void setAccountRechargeRv(){
        mAccountList=new ArrayList<>();
        baseAccountRecyclerAdapter=new BaseRecyclerAdapter<AccountRechargeNumBean.NumlistBean>(AccountRechargeActivity.this,mAccountList,R.layout.ly_activity_account_recharge_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, AccountRechargeNumBean.NumlistBean numlistBean, int position) {
                holder.setTxtColor(R.id.account_recharge_rv_item_tv,"#333333");
                if (isShowColor){
                    if (showPos==position){ //位置相同开始执行
                        if (numlistBean.getShow()){ //  第一次  是 true
                            holder.setTxtColor(R.id.account_recharge_rv_item_tv,"#F35C5D");
                            reNum = numlistBean.getReNum();
                            numlistBean.setShow(numlistBean.getShow());
                            // 设置成为false
                        }else {
                            holder.setTxtColor(R.id.account_recharge_rv_item_tv,"#333333");
                            reNum = "";
                            numlistBean.setShow(numlistBean.getShow());
                        }
                    }else {
                        numlistBean.setShow(false);
                    }
                }

                holder.setClick(R.id.account_recharge_rv_item_rl,numlistBean,position,baseAccountRecyclerAdapter);
                holder.setTxt(R.id.account_recharge_rv_item_tv,numlistBean.getReNme());
                holder.setTxt(R.id.account_recharge_rv_item_o_tv,numlistBean.getReRemark());
            }

            @Override
            public void clickEvent(int viewId, AccountRechargeNumBean.NumlistBean numlistBean, int position) {
                super.clickEvent(viewId, numlistBean, position);
                switch (viewId){
                    case R.id.account_recharge_rv_item_rl:
                        isShowColor=true;
                        showPos=position;
                        numlistBean.setShow(!numlistBean.getShow());
                        notifyDataSetChanged();
                        break;
                }
            }
        };
        account_recharge_rv.setAdapter(baseAccountRecyclerAdapter);
    }


    /**
     * 检查充值订单状态   -网络请求
     */
    private void initOrderCheck(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)) {
            LecoOkHttpUtil lecoOkHttpUtil = new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.CHECK_PAY_STATUS)
                    .addParams("orderNo",orderNo)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    //这里是上传出错的回调
                    LyLog.e(TAG,"检查充值订单状态异常 = "+e.getMessage());
                }
                @Override
                public void onResponse(String response) {
                    LyLog.i(TAG,"检查充值订单状态 = "+response);
                    Gson gson = new Gson();
                    DefaultBean parse=gson.fromJson(response,DefaultBean.class);
                    if ("OK".equals(parse.getStatus())){
                        if ("OK".equals(parse.getData())){
                            isSuccess="支付成功";
                        }else {
                            isSuccess="支付失败";
                        }
                        new CommomDialog(AccountRechargeActivity.this, R.style.dialog, isSuccess, new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog, boolean confirm) {
                                if (confirm){
                                    finish();
                                    dialog.dismiss();
                                }
                            }
                        }).setTitle("提示").show();

                    }else {
                        new CommomDialog(AccountRechargeActivity.this, R.style.dialog, "支付失败", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog, boolean confirm) {
                                if (confirm){
                                    finish();
                                    dialog.dismiss();
                                }
                            }
                        }).setTitle("提示").show();
                    }
                }
            });
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sentParsms(PayEntity payEntity) {//此方法类似于广播，任何地方都可以传递
        if (PayEntity.Status.PAY_SUCCESS.equals(payEntity.getStatus())){
//           支付成功
            initOrderCheck();

        }else if (PayEntity.Status.PAY_FAIL.equals(payEntity.getStatus())){
//            支付失败
            initOrderCheck();
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
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_account_recharge_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setTxtColor(R.id.account_recharge_rv_item_tv,"#333333");
                if (isShowColor){
                    if (showPos==position){ //位置相同开始执行
                        if (landDetailsNameBean.getShow()){ //  第一次  是 true
                            holder.setTxtColor(R.id.account_recharge_rv_item_tv,"#F35C5D");
                            landDetailsNameBean.setShow(landDetailsNameBean.getShow());
                            // 设置成为false
                        }else {
                            holder.setTxtColor(R.id.account_recharge_rv_item_tv,"#333333");
                            landDetailsNameBean.setShow(landDetailsNameBean.getShow());
                        }
                    }else {
                            landDetailsNameBean.setShow(false);
                    }
                }

                holder.setClick(R.id.account_recharge_rv_item_rl,landDetailsNameBean,position,baseRecyclerAdapter);
                holder.setTxt(R.id.account_recharge_rv_item_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.account_recharge_rv_item_o_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
            }

            @Override
            public void clickEvent(int viewId, LandDetailsNameBean landDetailsNameBean, int position) {
                super.clickEvent(viewId, landDetailsNameBean, position);
                switch (viewId){
                    case R.id.account_recharge_rv_item_rl:
                        isShowColor=true;
                        showPos=position;
                        landDetailsNameBean.setShow(!landDetailsNameBean.getShow());
                        notifyDataSetChanged();
                        break;
                }
            }
        };
        account_recharge_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean("100元","1000货币",false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("200元","2000货币",false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("500元","5000货币+500",false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("1000元","1万货币+1000",false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("2000元","2万货币+2000",false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("5000元","5万货币+5000",false);
        mList.add(landDetailsNameBean);
        return mList;
    }

}
