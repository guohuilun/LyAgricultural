package com.lyagricultural.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.AccountOrderBean;
import com.lyagricultural.bean.DefaultBean;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.SpUtils;
import com.lyagricultural.utils.WidthUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/6/4 0004 16:17
 */
public class AccountOrderActivity  extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "AccountOrderActivity";
    private RelativeLayout account_order_all_rl;
    private RelativeLayout account_order_seed_rl;
    private RelativeLayout account_order_prop_rl;
    private RelativeLayout account_order_land_rl;
    private RecyclerView account_order_rv;
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;
    private  PopupWindow popupWindow;
    private int pageIndex=1;
    private Boolean isPage=true;
    private BaseRecyclerAdapter<AccountOrderBean.OrderlistBean> orderlistBeanBaseRecyclerAdapter;
    private List<AccountOrderBean.OrderlistBean> mOrderList;
    private LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_order);
        setTitle("订单");
        mImageRight.setImageResource(R.mipmap.ce_shop_seed_t);
        setHeadRightVisibility(View.VISIBLE);
        mImageRight.setOnClickListener(this);
        initView();
    }

    private void initView(){
        account_order_all_rl=findViewById(R.id.account_order_all_rl);
        account_order_seed_rl=findViewById(R.id.account_order_seed_rl);
        account_order_prop_rl=findViewById(R.id.account_order_prop_rl);
        account_order_land_rl=findViewById(R.id.account_order_land_rl);
        account_order_rv=findViewById(R.id.account_order_rv);
        account_order_all_rl.setOnClickListener(this);
        account_order_seed_rl.setOnClickListener(this);
        account_order_prop_rl.setOnClickListener(this);
        account_order_land_rl.setOnClickListener(this);
        layoutManager=new LinearLayoutManager(this);
        account_order_rv.setLayoutManager(layoutManager);
//        setLandRv();
        initOrderAccount(pageIndex+"");
        setOrderList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_right:
                showPopupWindow();
                break;
            case R.id.account_order_all_rl:
                break;
            case R.id.account_order_seed_rl:
                break;
            case R.id.account_order_prop_rl:
                break;
            case R.id.account_order_land_rl:
                break;

        }
    }

    /**
     *  获取历史订单   -网络请求
     */
    private void initOrderAccount(final String PageCount){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_ORDER)
                    .addParams("UserId", SpUtils.getSp("userid",AccountOrderActivity.this,"LoginActivity"))
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
                holder.setClick(R.id.account_order_rv_rl,orderlistBean,position,baseRecyclerAdapter);
                holder.setImg(AccountOrderActivity.this,orderlistBean.getOrderUrl(),R.id.account_order_rv_iv);
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
                        startActivity(new Intent(AccountOrderActivity.this,AccountOrderDetailsActivity.class));
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




    private void showPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.ly_activity_account_order_pop, null);
        int screenWidth = WidthUtils.getScreenWidth(this);
        popupWindow=new PopupWindow(view,screenWidth/3, 400,false);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
        view.findViewById(R.id.account_order_pop_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });

        view.findViewById(R.id.account_order_pop_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;

                    case MotionEvent.ACTION_UP:
                        break;

                    case  MotionEvent.ACTION_MOVE:
                        break;
                    case  MotionEvent.ACTION_CANCEL:
                        break;
                }
                return false;
            }
        });
        popupWindow.showAsDropDown(mRlRight);

    }


    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_account_order_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setClick(R.id.account_order_rv_rl,landDetailsNameBean,position,baseRecyclerAdapter);
                holder.setBgResource(R.id.account_order_rv_iv,landDetailsNameBean.getLand_details_name_rv_iv());
                holder.setTxt(R.id.account_order_rv_order_number_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.account_order_rv_time_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
                holder.setTxt(R.id.account_order_rv_name_tv,landDetailsNameBean.getLand_details_name_rv_tv_t());
                holder.setTxt(R.id.account_order_rv_number_tv,landDetailsNameBean.getLand_details_name_rv_tv_tt());
                holder.setTxt(R.id.account_order_rv_number_o_tv,landDetailsNameBean.getLand_details_name_rv_tv_f());
                holder.setTxt(R.id.account_order_rv_money_tv,landDetailsNameBean.getLand_details_name_rv_tv_ff());
            }

            @Override
            public void clickEvent(int viewId, LandDetailsNameBean landDetailsNameBean, int position) {
                super.clickEvent(viewId, landDetailsNameBean, position);
                switch (viewId){
                    case R.id.account_order_rv_rl:
                        startActivity(new Intent(AccountOrderActivity.this,AccountOrderDetailsActivity.class));
                        break;
                }
            }
        };
        account_order_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_f,"订单号：2659265555","时间：2018-05-24","购买种子","数量：40","","200");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_land_bg_o,"订单号：2659265555","时间：2018-05-24","购买土地","名称：风光一号","","1000");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_land_bg_t,"订单号：2659265555","时间：2018-05-24","作物照料","实施日期：2018/5/25","作用土地：风光一号","320");
        mList.add(landDetailsNameBean);
        return mList;
    }
}
