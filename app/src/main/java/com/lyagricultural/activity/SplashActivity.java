package com.lyagricultural.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import com.lyagricultural.permissions.RuntimeRationale;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.ProgressDialogUtils;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.utils.SpUtils;
import com.lyagricultural.utils.TxUtils;
import com.tencent.bugly.crashreport.BuglyLog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;

/**
 * 作者Administrator on 2018/7/16 0016 10:35
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private ImageView iv_splash;
    private String DEVICE_ID;
    private  Boolean isShowPermission =false;
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
        if (isShowPermission==false){
            LyLog.i(TAG,"你来获取权限了吗？");
            setPermissionUtils();
        }
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


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_SET_ALIAS:
                    LyLog.i(TAG, "推送 = Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAlias(getApplicationContext(), (String)msg.obj, mAliasCallback);
                    break;

            }
        }
    };


    //以下是关于推送的别名返回参数
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    LyLog.i(TAG, "推送 = Set tag and alias success");
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    LyLog.i(TAG, "推送 = Failed to set alias and tags due to timeout. Try again after 60s.");
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    LyLog.i(TAG, "推送 = Failed with errorCode = "+code);
            }
            // ExampleUtil.showToast(logs, getApplicationContext());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;


    private void setPermissionUtils(){
        if (AndPermission.hasPermissions(this, Manifest.permission.READ_PHONE_STATE)){
            LyLog.i(TAG,"存在权限");
            isShowPermission=true;
            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            DEVICE_ID = tm.getDeviceId();
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, DEVICE_ID));
            LyLog.i(TAG, "DEVICE_ID = "+DEVICE_ID);
        }else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

    }

    /**
     * Request permissions.
     */
    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        LyLog.i(TAG,"获取权限成功");
                        isShowPermission=true;
                        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                        DEVICE_ID = tm.getDeviceId();
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, DEVICE_ID));
                        LyLog.i(TAG, "DEVICE_ID = "+DEVICE_ID);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        LyLog.i(TAG,"获取权限失败");
                    }
                })
                .start();
    }

}
