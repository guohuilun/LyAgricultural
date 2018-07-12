package com.lyagricultural.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.app.LyAgriculturalApplication;
import com.lyagricultural.bean.LoginActivityBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.SpUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/6/8 0008 11:08
 */
public class LoginActivity extends BaseActivity  implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    private static final String TAG = "LoginActivity";
    private EditText login_name_et;
    private EditText login_password_et;
    private CheckBox login_remenber_cb;
    private CheckBox login_auto_cb;
    private Button login_button;
    private RelativeLayout login_register_rl;
    private String loginCode="";
    private String loginPass="";
    private String loginType="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_login);
        setHeadVisibility(View.GONE);
        initView();
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
             loginCode = intent.getStringExtra("LoginCode");
             loginPass = intent.getStringExtra("LoginPass");
             loginType = intent.getStringExtra("LoginType");
        }
        login_name_et=findViewById(R.id.login_name_et);
        login_password_et=findViewById(R.id.login_password_et);
        login_remenber_cb=findViewById(R.id.login_remenber_cb);
        login_auto_cb=findViewById(R.id.login_auto_cb);
        login_button=findViewById(R.id.login_button);
        login_register_rl=findViewById(R.id.login_register_rl);
        login_button.setOnClickListener(this);
        login_register_rl.setOnClickListener(this);
        login_remenber_cb.setOnCheckedChangeListener(this);
        initUserInformation();
        initRegisterPhone();
    }

    /**
     * 初始化用户信息
     */
    private void initUserInformation() {
        String username = SpUtils.getSp("username",this,"LoginActivity");
        String password = SpUtils.getSp("password",this,"LoginActivity");
        if (!SpUtils.TextIsEmpty(username)) {
            login_name_et.setText(username);
            login_password_et.requestFocus();
            if (!SpUtils.TextIsEmpty(password)) {
                login_password_et.setText(password);
                login_remenber_cb.setChecked(true);
                login_password_et.setSelection(login_password_et.getText().toString().trim().length());
            }
        }
    }

    private void initRegisterPhone(){
        LyLog.i(TAG,"code = "+loginCode+" pass = "+loginPass);
        if (!"".equals(loginCode)
                && !"".equals(loginPass)
                && !"".equals(loginType)
                &&loginCode!=null
                &&loginPass!=null
                &&loginType!=null){
            login_name_et.setText(loginCode);
            login_password_et.setText(loginPass);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                if ("".equals(login_name_et.getText().toString().trim())&&"".equals(login_password_et.getText().toString().trim())){
                    LyToast.shortToast(this,"账户和密码不能为空");
                    return;
                }

                if (login_remenber_cb.isChecked()){
                    login_remenber_cb.setChecked(true);
                    SpUtils.saveSp("username", login_name_et.getText().toString().trim(),this,"LoginActivity");
                    SpUtils.saveSp("password", login_password_et.getText().toString().trim(),this,"LoginActivity");
                    if (login_auto_cb.isChecked()){
                        login_auto_cb.setChecked(true);
                        SpUtils.saveSp("username", login_name_et.getText().toString().trim(),this,"LoginActivity");
                        SpUtils.saveSp("password", login_password_et.getText().toString().trim(),this,"LoginActivity");
                    }else {
                        login_auto_cb.setChecked(false);
                    }
                }else {
                    login_remenber_cb.setChecked(false);
                    SpUtils.removeSharedPreference("username",this,"LoginActivity");
                    SpUtils.removeSharedPreference("password",this,"LoginActivity");
                }

                initLogin(login_name_et.getText().toString().trim(),login_password_et.getText().toString().trim(),"Phone");

                break;
            case R.id.login_register_rl:
//                测试bugly崩溃
//                CrashReport.testJavaCrash();
                startActivity(new Intent(LoginActivity.this,LoginRegisterActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.login_remenber_cb:
                if (isChecked){
                    if (!"".equals(login_name_et)&&!"".equals(login_password_et)){
                        SpUtils.saveSp("username", login_name_et.getText().toString().trim(),this,"LoginActivity");
                        SpUtils.saveSp("password", login_password_et.getText().toString().trim(),this,"LoginActivity");
                    }
                }else {
                    SpUtils.removeSharedPreference("username",this,"LoginActivity");
                    SpUtils.removeSharedPreference("password",this,"LoginActivity");
                }
                break;
            case R.id.login_auto_cb:
                if (isChecked){
                    if (!"".equals(login_name_et)&&!"".equals(login_password_et)){
                        SpUtils.saveSp("username", login_name_et.getText().toString().trim(),this,"LoginActivity");
                        SpUtils.saveSp("password", login_password_et.getText().toString().trim(),this,"LoginActivity");
                    }
                }else {
                    SpUtils.removeSharedPreference("username",this,"LoginActivity");
                    SpUtils.removeSharedPreference("password",this,"LoginActivity");
                }
                break;
        }
    }

    /**
     *  登录 -网络请求
     */
    private void initLogin(String LoginCode,String LoginPass ,String LoginType){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_LOGIN)
                    .addParams("LoginCode",LoginCode)
                    .addParams("LoginPass",LoginPass)
                    .addParams("LoginType",LoginType)
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
                            LoginActivityBean parse=gson.fromJson(response,LoginActivityBean.class);
                            if ("OK".equals(parse.getStatus())){
                                LyToast.shortToast(LoginActivity.this,parse.getMsg());
                                String spUserid = SpUtils.getSp("userid", LoginActivity.this, "LoginActivity");
                                if ("".equals(spUserid)){
                                    SpUtils.saveSp("userid", parse.getUserid(),LoginActivity.this,"LoginActivity");
                                }else{
                                    SpUtils.removeSharedPreference("userid",LoginActivity.this,"LoginActivity");
                                    SpUtils.saveSp("userid", parse.getUserid(),LoginActivity.this,"LoginActivity");
                                }
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            }else {
                                LyToast.shortToast(LoginActivity.this,parse.getMsg());
                            }
                        }
                    });
        }
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Toast.makeText(LoginActivity.this, "在按一次退出程序", Toast.LENGTH_SHORT).show();
                ((LyAgriculturalApplication) (getApplication())).activityManager.finishAllActivity();
                break;
        }
        return false;
    }
}
