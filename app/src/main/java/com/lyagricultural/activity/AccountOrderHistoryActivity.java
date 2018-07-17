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
import com.lyagricultural.bean.AccountOrderBean;
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
 * 作者Administrator on 2018/6/4 0004 16:17
 */
public class AccountOrderHistoryActivity extends BaseActivity{
    private static final String TAG = "AccountOrderHistoryActivity";
    private RecyclerView account_order_rv;
    private int pageIndex=1;
    private Boolean isPage=true;
    private BaseRecyclerAdapter<AccountOrderBean.OrderlistBean> orderlistBeanBaseRecyclerAdapter;
    private List<AccountOrderBean.OrderlistBean> mOrderList;
    private LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_history_order);
        setTitle("历史订单");
        initView();
    }

    private void initView(){
        account_order_rv=findViewById(R.id.account_order_rv);
        layoutManager=new LinearLayoutManager(this);
        account_order_rv.setLayoutManager(layoutManager);
        initOrderAccount(pageIndex+"");
        setOrderList();
    }

    /**
     *  获取历史订单   -网络请求
     */
    private void initOrderAccount(final String PageCount){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_ORDER)
                    .addParams("UserId", SpSimpleUtils.getSp("userid",AccountOrderHistoryActivity.this,"LoginActivity"))
                    .addParams("PageCount",PageCount)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取历史订单 = " +response);
                            Gson gson=new Gson();
                            AccountOrderBean parse=gson.fromJson(response,AccountOrderBean.class);
                            if ("OK".equals(parse.getStatus())){
                                isPage=true;
                                if ("1".equals(PageCount)){
                                   mOrderList.clear();
                                   mOrderList.addAll(parse.getOrderlist());
                                   orderlistBeanBaseRecyclerAdapter.notifyDataSetChanged();
                                }else {
                                    mOrderList.addAll(parse.getOrderlist());
                                    orderlistBeanBaseRecyclerAdapter.notifyDataSetChanged();
                                }
                            }else {
                                LyToast.shortToast(AccountOrderHistoryActivity.this,"暂无更多历史订单");
                                isPage=false;
                            }
                        }
                    });
        }
    }

    private void setOrderList(){
        mOrderList=new ArrayList<>();
        orderlistBeanBaseRecyclerAdapter=new BaseRecyclerAdapter<AccountOrderBean.OrderlistBean>
                (this,mOrderList,R.layout.ly_activity_account_order_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, AccountOrderBean.OrderlistBean orderlistBean, int position) {
                holder.setClick(R.id.account_order_rv_rl,orderlistBean,position,orderlistBeanBaseRecyclerAdapter);
                holder.setImg(AccountOrderHistoryActivity.this,orderlistBean.getOrderUrl(),R.id.account_order_rv_iv);
                holder.setTxt(R.id.account_order_rv_order_number_tv,orderlistBean.getOrderNo());
                holder.setTxt(R.id.account_order_rv_time_tv,orderlistBean.getInsertDt());
                holder.setTxt(R.id.account_order_rv_name_tv,orderlistBean.getOrderType());
                holder.setTxt(R.id.account_order_rv_money_tv,orderlistBean.getTotalAmt());
            }

            @Override
            public void clickEvent(int viewId, AccountOrderBean.OrderlistBean orderlistBean, int position) {
                super.clickEvent(viewId, orderlistBean, position);
                switch (viewId){
                    case R.id.account_order_rv_rl:
                        startActivity(new Intent(AccountOrderHistoryActivity.this,AccountOrderDetailsActivity.class));
                        break;
                }
            }
        };
        account_order_rv.setAdapter(orderlistBeanBaseRecyclerAdapter);
        account_order_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LyLog.i(TAG, "onScrollStateChanged: "+"状态="+newState+"位置"+lastVisibleItem+"===="+layoutManager.getItemCount() );
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+2>= layoutManager.getItemCount()) {
                    //加载更多
                    pageIndex++;
                    if (isPage==true){
                        initOrderAccount(pageIndex+"");
                        LyLog.i(TAG, "onScrollStateChanged: "+pageIndex);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }
}
