package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.yykj.mob.share.login.ShareLoginUtils;

import cn.sharesdk.wechat.friends.Wechat;

/**
 * 作者Administrator on 2018/6/8 0008 13:52
 */
public class LoginRegisterActivity extends BaseActivity  implements View.OnClickListener{
    private RelativeLayout register_weixin_rl;
    private RelativeLayout register_phone_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_login_register);
        setTitle("注册");
        initView();
    }

    private void initView(){
        register_weixin_rl=findViewById(R.id.register_weixin_rl);
        register_phone_rl=findViewById(R.id.register_phone_rl);
        register_weixin_rl.setOnClickListener(this);
        register_phone_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_weixin_rl:
//                ShareLoginUtils.login(this, Wechat.NAME);
                break;
            case R.id.register_phone_rl:
                finish();
                startActivity(new Intent(LoginRegisterActivity.this,LoginRegisterPhoneActivity.class));
                break;
        }
    }
}
