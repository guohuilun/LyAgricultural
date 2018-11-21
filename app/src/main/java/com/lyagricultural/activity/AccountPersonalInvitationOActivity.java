package com.lyagricultural.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.AccountPersonalInvitationBean;
import com.lyagricultural.constant.AppConstant;
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
 * 作者Administrator on 2018/6/5 0005 14:08
 */
public class AccountPersonalInvitationOActivity extends BaseActivity{
    private static final String TAG = "AccountPersonalInvitationOActivity";
    private RecyclerView pi_rv;
    private BaseRecyclerAdapter<AccountPersonalInvitationBean.UserinfoBean> mAccountRecyclerAdapter;
    private List<AccountPersonalInvitationBean.UserinfoBean> mAccountList;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_personal_invitation);
        setTitle("邀请详情");
        initView();
    }

    private void initView(){
        Intent intent = getIntent();
        if (intent!=null){
             userId = intent.getStringExtra("userId");
        }
        pi_rv=findViewById(R.id.pi_rv);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        pi_rv.setLayoutManager(layoutManager);
        initInUser();
        setMRecyclerAdapter();
    }



    /**
     *  获取被邀请人列表   -网络请求
     */
    private void initInUser(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_IN_USER_INFO_SELECT)
                    .addParams("UserId", userId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取被邀请人列表 = " +response);
                            Gson gson=new Gson();
                            AccountPersonalInvitationBean parse = gson.fromJson(response,AccountPersonalInvitationBean.class);
                            if ("OK".equals(parse.getStatus())){
                                mAccountList.clear();
                                mAccountList.addAll(parse.getUserinfo());
                                mAccountRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }


    private void setMRecyclerAdapter(){
        mAccountList = new ArrayList<>();
        mAccountRecyclerAdapter=new BaseRecyclerAdapter<AccountPersonalInvitationBean.UserinfoBean>
                (AccountPersonalInvitationOActivity.this,mAccountList,R.layout.ly_activity_account_personal_invitation_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, AccountPersonalInvitationBean.UserinfoBean userinfoBean, int position) {
                holder.setTxt(R.id.pi_rv_item_name_tv,userinfoBean.getUserNme());
                holder.setTxt(R.id.pi_rv_item_time_tv,userinfoBean.getInDt());
                holder.setTxt(R.id.pi_rv_item_num_tv,userinfoBean.getInCount());
                holder.setClick(R.id.pi_rv_ll,userinfoBean,position,mAccountRecyclerAdapter);
            }

            @Override
            public void clickEvent(int viewId, AccountPersonalInvitationBean.UserinfoBean userinfoBean, int position) {
                super.clickEvent(viewId, userinfoBean, position);
                switch (viewId){
                    case R.id.pi_rv_ll:
                        if ("0".equals(userinfoBean.getInCount())){
                            LyToast.shortToast(AccountPersonalInvitationOActivity.this,"暂未邀请客户");
                        }else {
                            startActivity(new Intent(AccountPersonalInvitationOActivity.this,AccountPersonalInvitationTActivity.class)
                                    .putExtra("userId",userinfoBean.getUserId()));
                        }
                        break;
                }
            }
        };
        pi_rv.setAdapter(mAccountRecyclerAdapter);
    }


}
