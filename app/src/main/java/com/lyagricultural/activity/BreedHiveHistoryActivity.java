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
import com.lyagricultural.bean.EventBusBreedHiveBean;
import com.lyagricultural.cebean.LandDetailsNameBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


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

    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;

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
        setLandRv();
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


    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_breed_hive_history_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landFragmentBean, int position) {
                holder.setTxt(R.id.breed_hive_history_time_tv,landFragmentBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.breed_hive_history_temperature_tv,landFragmentBean.getLand_details_name_rv_tv_o());
                holder.setTxt(R.id.breed_hive_history_humidity_tv,landFragmentBean.getLand_details_name_rv_tv_t());
                holder.setTxt(R.id.breed_hive_history_weight_tv,landFragmentBean.getLand_details_name_rv_tv_tt());
                holder.setTxt(R.id.breed_hive_history_fly_out_in_tv,landFragmentBean.getLand_details_name_rv_tv_f());
            }
        };
        breed_hive_history_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landFragmentBean=new LandDetailsNameBean
                ("2018-6-12 10:25:43","35℃"
                        ,"70%","9.5kg","500/100");
        mList.add(landFragmentBean);
        landFragmentBean=new LandDetailsNameBean
                ("2018-8-12 11:35:20","36℃"
                        ,"65%","10.5kg","300/200");
        mList.add(landFragmentBean);
        landFragmentBean=new LandDetailsNameBean
                ("2018-12-26 9:05:12","37℃"
                        ,"72%","3.5kg","100/40");
        mList.add(landFragmentBean);
        return mList;
    }



}
