package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.EventBusLandDetailsBean;
import com.lyagricultural.bean.LandHistoryBean;
import com.lyagricultural.cebean.LandFragmentBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/6/5 0005 17:00
 */
public class LandHistoryActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "LandHistoryActivity";
    private RecyclerView land_history_rv;
    private String goodsId;
    private  BaseRecyclerAdapter<LandFragmentBean> baseRecyclerAdapter;
    private  List<LandFragmentBean> mList;

    private BaseRecyclerAdapter<LandHistoryBean.LoginfoBean> historyRecyclerAdapter;
    private List<LandHistoryBean.LoginfoBean>mHistoryList;
    private int pageIndex=1;
    private LinearLayoutManager layoutManager;
    private Boolean isPage=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_history);
        setTitle("历史动态");
        setHeadRightTxVisibility(View.VISIBLE);
        setRightTitle("清空");
        initView();
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
            goodsId = intent.getStringExtra("goodsId");
        }
        land_history_rv=findViewById(R.id.land_history_rv);
        layoutManager=new LinearLayoutManager(this);
        land_history_rv.setLayoutManager(layoutManager);
        mRlTxRight.setOnClickListener(this);
        initLandLogSelect(pageIndex+"");
        setLandHistoryListRv();
//        setLandRv();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_right_txt:

                break;
        }
    }

    /**
     *  获取用户历史土地动态   -网络请求
     */
    private void initLandLogSelect(final String PageCount){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_LAND_LOG_SELECT)
                    .addParams("landId", goodsId)
                    .addParams("pageCount",PageCount)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取用户历史土地动态 = " +response);
                            Gson gson=new Gson();
                            LandHistoryBean parse=gson.fromJson(response,LandHistoryBean.class);
                            if ("OK".equals(parse.getStatus())){
                                isPage=true;
                                if ("1".equals(PageCount)){
                                    mHistoryList.clear();
                                    mHistoryList.addAll(parse.getLoginfo());
                                    historyRecyclerAdapter.notifyDataSetChanged();
                                }else {
                                    mHistoryList.addAll(parse.getLoginfo());
                                    historyRecyclerAdapter.notifyDataSetChanged();
                                }
                            }else {
                                LyToast.shortToast(LandHistoryActivity.this,"暂无更多历史土地动态");
                                isPage=false;
                            }
                        }
                    });
        }
    }

  private void setLandHistoryListRv(){
      mHistoryList=new ArrayList<>();
      historyRecyclerAdapter=new BaseRecyclerAdapter<LandHistoryBean.LoginfoBean>(this,mHistoryList,R.layout.ly_activity_land_history_rv_item) {
          @Override
          public void bindData(BaseRecyclerViewHolder holder, LandHistoryBean.LoginfoBean loginfoBean, int position) {
              holder.setTxt(R.id.land_history_operation_tv,loginfoBean.getLogType());
              holder.setTxt(R.id.land_history_remark_tv,loginfoBean.getLogRemark());
              holder.setTxt(R.id.land_history_time_tv,loginfoBean.getInsertDt());
          }
      };
      land_history_rv.setAdapter(historyRecyclerAdapter);
      land_history_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
          private int lastVisibleItem;
          @Override
          public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
              super.onScrollStateChanged(recyclerView, newState);
              LyLog.i(TAG, "onScrollStateChanged: "+"状态="+newState+"位置"+(lastVisibleItem+2)+"===="+layoutManager.getItemCount() );
              if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+2>= layoutManager.getItemCount()) {
                  //加载更多
                  pageIndex++;
                  if (isPage==true){
                      initLandLogSelect(pageIndex+"");
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new EventBusLandDetailsBean("OK"));
    }

    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandFragmentBean>(this,getLandRv(),R.layout.ly_activity_land_history_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandFragmentBean landFragmentBean, int position) {

            }

            @Override
            public void clickEvent(int viewId, LandFragmentBean landFragmentBean, int position) {
                super.clickEvent(viewId, landFragmentBean, position);
                switch (viewId){

                }
            }
        };
        land_history_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandFragmentBean> getLandRv(){
        mList=new ArrayList<>();
        LandFragmentBean landFragmentBean=new LandFragmentBean();
        mList.add(landFragmentBean);
        return mList;
    }


}
