package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.lyagricultural.utils.ProgressDialogUtils;
import com.lyagricultural.utils.SpSimpleUtils;
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
    private boolean isChecked=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_login);
        //             获取窗口属性管理
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
//             窗口的属性设置
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        status_bar_view.setVisibility(View.GONE);
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
        login_auto_cb.setOnCheckedChangeListener(this);
        initUserInformation();
        initRegisterPhone();
    }

    /**
     * 初始化用户信息
     */
    private void initUserInformation() {
        String auto = SpSimpleUtils.getSp("auto", this, "LoginActivity");
        String username = SpSimpleUtils.getSp("username",this,"LoginActivity");
        String password = SpSimpleUtils.getSp("password",this,"LoginActivity");
        if (!SpSimpleUtils.TextIsEmpty(auto)){
            login_remenber_cb.setChecked(true);
            login_auto_cb.setChecked(true);
            if (!SpSimpleUtils.TextIsEmpty(username)){
                login_name_et.setText(username);
            }
            if (!SpSimpleUtils.TextIsEmpty(password)){
                login_password_et.setText(password);
            }


        }else {
            if (!SpSimpleUtils.TextIsEmpty(username)) {
                login_name_et.setText(username);
                login_password_et.requestFocus();
                if (!SpSimpleUtils.TextIsEmpty(password)) {
                    login_password_et.setText(password);
                    login_remenber_cb.setChecked(true);
                    login_password_et.setSelection(login_password_et.getText().toString().trim().length());
                }
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
                SpSimpleUtils.removeSharedPreference("auto",this,"LoginActivity");
                SpSimpleUtils.removeSharedPreference("username",this,"LoginActivity");
                SpSimpleUtils.removeSharedPreference("password",this,"LoginActivity");
                if (login_auto_cb.isChecked()){
                    LyLog.i(TAG,"选中自动登录的情况下选中记住密码 ");
                    SpSimpleUtils.saveSp("auto", "自动登录",this,"LoginActivity");
                    SpSimpleUtils.saveSp("username", login_name_et.getText().toString().trim(),this,"LoginActivity");
                    SpSimpleUtils.saveSp("password", login_password_et.getText().toString().trim(),this,"LoginActivity");
                }else {
                    if (login_remenber_cb.isChecked()){
                        LyLog.i(TAG,"未选中自动登录的情况下选中记住密码");
                        SpSimpleUtils.saveSp("username", login_name_et.getText().toString().trim(),this,"LoginActivity");
                        SpSimpleUtils.saveSp("password", login_password_et.getText().toString().trim(),this,"LoginActivity");
                    }
                }

                if (isChecked){
                    ProgressDialogUtils.showProgressDialog(this,"正在登录，请稍后...");
                    setProcessFlag();
                    initLogin(login_name_et.getText().toString().trim(),login_password_et.getText().toString().trim(),"Phone");
                    new TimeThread().start();
                }

                break;
            case R.id.login_register_rl:
//                测试bugly崩溃
//                CrashReport.testJavaCrash();
                startActivity(new Intent(LoginActivity.this,LoginRegisterActivity.class));
                break;
        }
    }


    private synchronized void setProcessFlag() {
        isChecked = false;
    }

    //  使用线程防止多次点击
    private class TimeThread extends Thread {
        public void run() {
            try {
                sleep(1000);
                isChecked = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.login_auto_cb:
                if (isChecked){
                    login_remenber_cb.setChecked(true);
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
                                ProgressDialogUtils.closeProgressDialog();
                                LyToast.shortToast(LoginActivity.this,parse.getMsg());
                                String spUserid = SpSimpleUtils.getSp("userid", LoginActivity.this, "LoginActivity");
                                if ("".equals(spUserid)){
                                    SpSimpleUtils.saveSp("userid", parse.getUserid(),LoginActivity.this,"LoginActivity");
                                }else{
                                    SpSimpleUtils.removeSharedPreference("userid",LoginActivity.this,"LoginActivity");
                                    SpSimpleUtils.saveSp("userid", parse.getUserid(),LoginActivity.this,"LoginActivity");
                                }
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            }else {
                                ProgressDialogUtils.closeProgressDialog();
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
