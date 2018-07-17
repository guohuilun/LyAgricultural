package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.LoginActivityBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.constant.MsgConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.ProgressDialogUtils;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.utils.SpUtils;
import com.lyagricultural.utils.TxUtils;
import com.tencent.bugly.crashreport.BuglyLog;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/7/16 0016 10:35
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private ImageView iv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, MsgConstant.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.ly_activity_splash);
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
        iv_splash=findViewById(R.id.iv_splash);
        initLogin();
    }



    /**
     *  登录 -网络请求
     */
    private void initLogin(){
        try{
            if (CheckNetworkUtils.checkNetworkAvailable(this)) {
                String auto = SpSimpleUtils.getSp("auto", this, "LoginActivity");
                if (!TxUtils.TextIsEmpty(auto)&& "自动登录".equals(auto)) {
                    ProgressDialogUtils.showProgressDialog(this,"正在自动登录，请稍后...");
                    String username = SpSimpleUtils.getSp("username",this,"LoginActivity");
                    String password = SpSimpleUtils.getSp("password",this,"LoginActivity");
                    LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
                    lecoOkHttpUtil.post().url(AppConstant.APP_USER_LOGIN)
                            .addParams("LoginCode",username)
                            .addParams("LoginPass",password)
                            .addParams("LoginType","Phone")
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
                                        LyToast.shortToast(SplashActivity.this,parse.getMsg());
                                        String spUserid = SpSimpleUtils.getSp("userid", SplashActivity.this, "LoginActivity");
                                        if ("".equals(spUserid)){
                                            SpSimpleUtils.saveSp("userid", parse.getUserid(),SplashActivity.this,"LoginActivity");
                                        }else{
                                            SpSimpleUtils.removeSharedPreference("userid",SplashActivity.this,"LoginActivity");
                                            SpSimpleUtils.saveSp("userid", parse.getUserid(),SplashActivity.this,"LoginActivity");
                                        }
                                        new Handler().postDelayed(new Runnable() {

                                            @Override
                                            public void run() {
                                                startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                                                finish();
                                            }
                                        }, 2000);
                                    }else {
                                        ProgressDialogUtils.closeProgressDialog();
                                        LyToast.shortToast(SplashActivity.this,parse.getMsg());
                                    }
                                }
                            });
                }else {
//             当不是自动登录的情况下
                    enterLoginActivity();
                }
            }else {
//             当没有网络的情况下
                enterLoginActivity();
            }
        }catch (IllegalStateException e){
            BuglyLog.e("Wu","网络状态不对 = "+e.getMessage());
            LyToast.shortToast(this,"您的网络不稳定请稍后重新登录.....");
        }catch (NullPointerException e){
            BuglyLog.e("Wu","自动登录空指针异常 = "+e.getMessage());
        }
    }

    private void enterLoginActivity(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }
        }, 2000);
    }


}
