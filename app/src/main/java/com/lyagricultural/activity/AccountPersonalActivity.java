package com.lyagricultural.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;

/**
 * 作者Administrator on 2018/6/5 0005 14:08
 */
public class AccountPersonalActivity extends BaseActivity {
    private RelativeLayout account_personal_head_rl;
    private RelativeLayout account_personal_name_rl;
    private TextView account_personal_name_tv;
    private RelativeLayout account_personal_sex_rl;
    private TextView account_personal_sex_tv;
    private RelativeLayout account_personal_phone_rl;
    private TextView account_personal_phone_tv;
    private RelativeLayout account_personal_wei_xin_rl;
    private TextView account_personal_wei_xin_tv;
    private RelativeLayout bottom_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_personal);
        setTitle("我的信息");
        initView();
    }

    private void initView(){
        account_personal_head_rl=findViewById(R.id.account_personal_head_rl);
        account_personal_name_rl=findViewById(R.id.account_personal_name_rl);
        account_personal_name_tv=findViewById(R.id.account_personal_name_tv);
        account_personal_sex_rl=findViewById(R.id.account_personal_sex_rl);
        account_personal_sex_tv=findViewById(R.id.account_personal_sex_tv);
        account_personal_phone_rl=findViewById(R.id.account_personal_phone_rl);
        account_personal_phone_tv=findViewById(R.id.account_personal_phone_tv);
        account_personal_wei_xin_rl=findViewById(R.id.account_personal_wei_xin_rl);
        account_personal_wei_xin_tv=findViewById(R.id.account_personal_wei_xin_tv);
        bottom_rl=findViewById(R.id.bottom_rl);
    }
}
