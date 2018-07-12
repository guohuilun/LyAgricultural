package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.EventBusLandDetailsBean;
import com.lyagricultural.cebean.LandFragmentBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者Administrator on 2018/6/5 0005 17:00
 */
public class LandHistoryActivity extends BaseActivity {
    private RecyclerView land_history_rv;
    private  BaseRecyclerAdapter<LandFragmentBean> baseRecyclerAdapter;
    private  List<LandFragmentBean> mList;


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
        land_history_rv=findViewById(R.id.land_history_rv);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        land_history_rv.setLayoutManager(layoutManager);
        setLandRv();
    }


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new EventBusLandDetailsBean("OK"));
    }
}
