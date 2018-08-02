package com.lyagricultural.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.LoginRegisterPhoneActivityBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/6/19 0019 10:55
 */
public class LoginRegisterPhoneActivity extends BaseActivity  implements View.OnClickListener{
    private static final String TAG = "LoginRegisterPhoneActivity";
    private EditText login_register_phone_et;
    private EditText login_register_phone_password_et;
    private EditText login_register_phone_invitation_code_et;
    private Button login_register_phone_complete_button;
    private String code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_login_register_phone);
        setTitle("手机注册");
        initView();
    }

    private void initView(){
        login_register_phone_et=findViewById(R.id.login_register_phone_et);
        login_register_phone_password_et=findViewById(R.id.login_register_phone_password_et);
        login_register_phone_invitation_code_et=findViewById(R.id.login_register_phone_invitation_code_et);
        login_register_phone_complete_button=findViewById(R.id.login_register_phone_complete_button);
        login_register_phone_complete_button.setOnClickListener(this);
    }


    @Override
    public void onHeadLeftRl(View v) {
        super.onHeadLeftRl(v);
        hintKeyboard();
        finish();
    }

    /**
     * 关闭软键盘
     */
    private void hintKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_register_phone_complete_button:
                int num = (int) ((Math.random() * 9 + 1) * 100000);
                LyLog.i(TAG,"随机数 = "+num);
                if ("".equals(login_register_phone_et.getText().toString().trim())){
                    LyToast.shortToast(this,"输入手机号不能为空");
                    return;
                }

                if ("".equals(login_register_phone_password_et.getText().toString().trim())){
                    LyToast.shortToast(this,"输入密码不能为空");
                    return;
                }
                code=login_register_phone_invitation_code_et.getText().toString().trim();
                LyLog.i(TAG,"邀请码的值 = "+code);
                initLoginRegister(login_register_phone_et.getText().toString().trim(),
                        login_register_phone_password_et.getText().toString().trim(),
                        "Phone","android用户"+num,"0");
                break;
        }
    }

    /**
     *  登录注册 -网络请求
     */
    private void initLoginRegister(final String LoginCode,final String LoginPass ,final String LoginType ,final String UserName ,final String UserSex){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_CREATE)
                    .addParams("LoginCode",LoginCode)
                    .addParams("LoginPass",LoginPass)
                    .addParams("LoginType",LoginType)
                    .addParams("UserName",UserName)
                    .addParams("UserSex",UserSex)
                    .addParams("OtherInCode",code)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,response);
                            Gson gson=new Gson();
                            LoginRegisterPhoneActivityBean parse=gson.fromJson(response,LoginRegisterPhoneActivityBean.class);
                            if ("OK".equals(parse.getStatus())){

                                LyToast.shortToast(LoginRegisterPhoneActivity.this,parse.getMsg());
                                finish();
                                startActivity(new Intent(LoginRegisterPhoneActivity.this,LoginActivity.class)
                                        .putExtra("LoginCode",LoginCode)
                                        .putExtra("LoginPass",LoginPass)
                                        .putExtra("LoginType",LoginType)
                                );
                            }else {
                                LyToast.shortToast(LoginRegisterPhoneActivity.this,parse.getMsg());
                            }
                        }
                    });
        }
    }

}
