package com.lyagricultural.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.AccountMonetaryDetailsBean;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.SpSimpleUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/6/5 0005 13:49
 */
public class AccountMonetaryDetailsActivity extends BaseActivity {
    private static final String TAG = "AccountMonetaryDetailsActivity";
    private RecyclerView account_monetary_details_rv;
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;

    private BaseRecyclerAdapter<AccountMonetaryDetailsBean.AccinfoBean> accountRecyclerAdapter;
    private List<AccountMonetaryDetailsBean.AccinfoBean> mAccountList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_monetary_details);
        setTitle("货币明细");
        initView();
    }

    private void initView(){
        account_monetary_details_rv=findViewById(R.id.account_monetary_details_rv);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        account_monetary_details_rv.setLayoutManager(layoutManager);
        initAccountMonetaryDetails();
        setAccountList();
//        setLandRv();
    }

    /**
     *  获取用户账户明细  -网络请求
     */
    private void initAccountMonetaryDetails(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_ACCOUNT_SELECT)
                    .addParams("userId", SpSimpleUtils.getSp("userid",this,"LoginActivity"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取用户账户明细 = " +response);
                            Gson gson=new Gson();
                            AccountMonetaryDetailsBean parse=gson.fromJson(response,AccountMonetaryDetailsBean.class);
                            if ("OK".equals(parse.getStatus())){
                                mAccountList.clear();
                                mAccountList.addAll(parse.getAccinfo());
                                accountRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    private void setAccountList(){
        mAccountList=new ArrayList<>();
        accountRecyclerAdapter=new BaseRecyclerAdapter<AccountMonetaryDetailsBean.AccinfoBean>
                (this,mAccountList,R.layout.ly_activity_account_monetary_details_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, AccountMonetaryDetailsBean.AccinfoBean accinfoBean, int position) {
                holder.setTxt(R.id.account_monetary_details_rv_item_name_tv,accinfoBean.getAcRemark());
                holder.setTxt(R.id.account_monetary_details_rv_item_time_tv,accinfoBean.getInsertDt());
                if ("False".equals(accinfoBean.getOnOff())){
                    holder.setTxt(R.id.account_monetary_details_rv_item_money_tv,"-"+accinfoBean.getAcNum());
                }else {
                    holder.setTxt(R.id.account_monetary_details_rv_item_money_tv,"+"+accinfoBean.getAcNum());
                }

            }
        };
        account_monetary_details_rv.setAdapter(accountRecyclerAdapter);
    }


    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_account_monetary_details_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setTxt(R.id.account_monetary_details_rv_item_name_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.account_monetary_details_rv_item_time_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
                holder.setTxt(R.id.account_monetary_details_rv_item_money_tv,landDetailsNameBean.getLand_details_name_rv_tv_t());
            }
        };
        account_monetary_details_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean("开垦","2017-10-29 20:16:34","+5000.00");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("除草","2017-10-18 13:12:34","-10.00");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("购买土地","2017-10-13 09:16:24","-200.00");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean("充值","2017-09-02 08:16:34","+50.00");
        mList.add(landDetailsNameBean);
        return mList;
    }
}
