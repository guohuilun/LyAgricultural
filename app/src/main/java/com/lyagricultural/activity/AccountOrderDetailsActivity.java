package com.lyagricultural.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者Administrator on 2018/6/4 0004 17:13
 */
public class AccountOrderDetailsActivity extends BaseActivity {
    private TextView account_order_details_number_tv;
    private TextView account_order_details_time_tv;
    private RecyclerView account_order_details_rv;
    private TextView account_order_details_number_o_tv;
    private TextView account_order_details_time_place_tv;
    private TextView account_order_details_mode_tv;
    private TextView account_order_details_time_pay_tv;
    private TextView account_order_details_time_sow_tv;
    private TextView account_order_details_all_money_tv;
    private TextView account_order_details_discount_money_tv;
    private TextView account_order_details_money_tv;
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_order_details);
        setTitle("订单详情");
        initView();
    }

    private void initView(){
        account_order_details_number_tv=findViewById(R.id.account_order_details_number_tv);
        account_order_details_time_tv=findViewById(R.id.account_order_details_time_tv);
        account_order_details_rv=findViewById(R.id.account_order_details_rv);
        account_order_details_number_o_tv=findViewById(R.id.account_order_details_number_o_tv);
        account_order_details_time_place_tv=findViewById(R.id.account_order_details_time_place_tv);
        account_order_details_mode_tv=findViewById(R.id.account_order_details_mode_tv);
        account_order_details_time_pay_tv=findViewById(R.id.account_order_details_time_pay_tv);
        account_order_details_time_sow_tv=findViewById(R.id.account_order_details_time_sow_tv);
        account_order_details_all_money_tv=findViewById(R.id.account_order_details_all_money_tv);
        account_order_details_discount_money_tv=findViewById(R.id.account_order_details_discount_money_tv);
        account_order_details_money_tv=findViewById(R.id.account_order_details_money_tv);
        RecyclerView.LayoutManager layoutManager=new FullyLinearLayoutManager(this);
        account_order_details_rv.setLayoutManager(layoutManager);
        setLandRv();
    }



    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_account_order_details_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setBgResource(R.id.account_order_details_rv_iv,landDetailsNameBean.getLand_details_name_rv_iv());
                holder.setTxt(R.id.account_order_details_rv_name_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.account_order_details_rv_number_tv,landDetailsNameBean.getLand_details_name_rv_tv_o());
                holder.setTxt(R.id.account_order_details_rv_money_tv,landDetailsNameBean.getLand_details_name_rv_tv_t());
            }
        };
        account_order_details_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_o,"荷兰进口胡萝卜种子","数量：20粒","10");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_t,"黄瓜种子","数量：2200粒","200");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_tt,"大豆种子","数量：1000粒","300");
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_shop_seed_f,"西瓜种子","数量：300粒","100");
        mList.add(landDetailsNameBean);
        return mList;
    }
}
