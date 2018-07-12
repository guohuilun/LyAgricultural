package com.lyagricultural.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;

/**
 * 作者Administrator on 2018/6/5 0005 10:24
 */
public class AccountSeedOutDetailsActivity extends BaseActivity {
    private ImageView iv_default;
    private TextView account_seed_out_details_time_tv;
    private TextView account_seed_out_details_pick_people_tv;
    private TextView account_seed_out_details_packing_people_tv;
    private TextView account_seed_out_details_consignee_tv;
    private TextView account_seed_out_details_weight_tv;
    private TextView account_seed_out_details_phone_tv;
    private TextView account_seed_out_details_address_tv;
    private TextView account_seed_out_details_inventory_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_seed_out_details);
        setTitle("派送详情");
        initView();
    }

    private void initView(){
        iv_default=findViewById(R.id.iv_default);
        account_seed_out_details_time_tv=findViewById(R.id.account_seed_out_details_time_tv);
        account_seed_out_details_pick_people_tv=findViewById(R.id.account_seed_out_details_pick_people_tv);
        account_seed_out_details_packing_people_tv=findViewById(R.id.account_seed_out_details_packing_people_tv);
        account_seed_out_details_consignee_tv=findViewById(R.id.account_seed_out_details_consignee_tv);
        account_seed_out_details_weight_tv=findViewById(R.id.account_seed_out_details_weight_tv);
        account_seed_out_details_phone_tv=findViewById(R.id.account_seed_out_details_phone_tv);
        account_seed_out_details_address_tv=findViewById(R.id.account_seed_out_details_address_tv);
        account_seed_out_details_inventory_tv=findViewById(R.id.account_seed_out_details_inventory_tv);
        iv_default.setImageResource(R.mipmap.ce_shop_seed_t);

    }
}
