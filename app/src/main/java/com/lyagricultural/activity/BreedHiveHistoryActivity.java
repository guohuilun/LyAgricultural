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
import com.lyagricultural.bean.EventBusBreedHiveBean;
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
public class BreedHiveHistoryActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "BreedHiveHistoryActivity";
    private RecyclerView breed_hive_history_rv;
    private String goodsId;

    private int pageIndex=1;
    private LinearLayoutManager layoutManager;
    private Boolean isPage=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_breed_hive_history);
        setTitle("历史状态");
        setHeadRightTxVisibility(View.VISIBLE);
        setRightTitle("清空");
        initView();
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
            goodsId = intent.getStringExtra("goodsId");
        }
        breed_hive_history_rv=findViewById(R.id.breed_hive_history_rv);
        layoutManager=new LinearLayoutManager(this);
        breed_hive_history_rv.setLayoutManager(layoutManager);
        mRlTxRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_right_txt:

                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new EventBusBreedHiveBean("OK"));
    }



}
