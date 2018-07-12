package com.lyagricultural.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.utils.VersionUtil;

/**
 * 作者Administrator on 2018/6/4 0004 17:42
 */
public class AccountAboutUsActivity extends BaseActivity {
    private TextView account_about_version_number_tv;
    private TextView account_about_tel_phone_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_about_us);
        setTitle("关于自留地");
        initView();
    }

    private void initView(){
        account_about_version_number_tv=findViewById(R.id.account_about_version_number_tv);
        account_about_tel_phone_tv=findViewById(R.id.account_about_tel_phone_tv);
        String versionName = VersionUtil.getVersionName(this);
        account_about_version_number_tv.setText(versionName);
    }
}
