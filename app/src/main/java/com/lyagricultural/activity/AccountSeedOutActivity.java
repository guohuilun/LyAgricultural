package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.cebean.LandDetailsNameBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者Administrator on 2018/6/5 0005 10:05
 */
public class AccountSeedOutActivity extends BaseActivity {
    private RecyclerView account_seed_out_rv;
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_seed_out);
        setTitle("派送");
        initView();
    }

    private void initView(){
        account_seed_out_rv=findViewById(R.id.account_seed_out_rv);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        account_seed_out_rv.setLayoutManager(layoutManager);
        setLandRv();
    }

    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_account_seed_out_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setClick(R.id.account_seed_out_rv_rl,landDetailsNameBean,position,baseRecyclerAdapter);
                holder.setBgResource(R.id.iv_default,landDetailsNameBean.getLand_details_name_rv_iv());
                holder.setTxt(R.id.account_seed_out_rv_name_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.account_seed_out_rv_pick_time_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
                holder.setTxt(R.id.account_seed_out_rv_seed_time_tv,landDetailsNameBean.getLand_details_name_rv_tv_t());
                holder.setTxt(R.id.account_seed_out_rv_sign_in_time_tv,landDetailsNameBean.getLand_details_name_rv_tv_tt());
                holder.setTxt(R.id.account_seed_out_rv_seed_halfway_time_tv,landDetailsNameBean.getLand_details_name_rv_tv_f());
            }

            @Override
            public void clickEvent(int viewId, LandDetailsNameBean landDetailsNameBean, int position) {
                super.clickEvent(viewId, landDetailsNameBean, position);
                switch (viewId){
                    case R.id.account_seed_out_rv_rl:
                        startActivity(new Intent(AccountSeedOutActivity.this,AccountSeedOutDetailsActivity.class));
                        break;
                }
            }
        };
        account_seed_out_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_t,"荷兰进口胡萝卜种子","采摘时间：2018-5-20 08:56:30","派送时间：2018-5-20 10:30:15","签收时间：","派送中");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_tt,"2018-6-20蔬菜采摘","采摘时间：2018-5-20 08:56:30","派送时间：2018-5-20 10:30:15","签收时间：","派送中");
        mList.add(landDetailsNameBean);
        return mList;
    }
}
