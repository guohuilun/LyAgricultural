package com.lyagricultural.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.adapter.ViewPagerImageAdapter;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.ImageBean;
import com.lyagricultural.bean.LoginActivityBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.constant.MsgConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.ImageUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.ProgressDialogUtils;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.utils.TxUtils;
import com.tencent.bugly.crashreport.BuglyLog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/7/16 0016 10:35
 */
public class AdvertisementActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AdvertisementActivity";
    private ViewPager splash_vp;
    private TextView splash_seconds_tv;
    private ImageView splash_start_iv;

    private SharedPreferences sharepreferences;//实例化 SharedPreferences
    private SharedPreferences.Editor editor;
    private String startImage;
    private int seconds=3;
    private List<ImageView> mImgList;
    private ImageView  imageView;
    private Boolean canHandler=true;
    private Boolean isOnPageChange=true;
    private Boolean canTrans=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_advertisement);
        //             获取窗口属性管理
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
//             窗口的属性设置
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        status_bar_view.setVisibility(View.GONE);
        // 初始化 SharedPreferences 储存
        sharepreferences=this.getSharedPreferences("check", MODE_PRIVATE);
        //将SharedPreferences 储存 可编辑化
        editor=sharepreferences.edit();
        setHeadVisibility(View.GONE);
        initView();
    }

    private void initView(){
        splash_vp=findViewById(R.id.splash_vp);
        splash_seconds_tv=findViewById(R.id.splash_seconds_tv);
        splash_start_iv=findViewById(R.id.splash_start_iv);
        splash_seconds_tv.setText("跳过"+seconds+"S");
        splash_seconds_tv.setOnClickListener(this);
        initImg();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.splash_seconds_tv:
                canHandler=false;
                initLogin();
                break;
        }
    }



    private void isFirstLoadSet(){
        //从SharedPreferences中获取是否第一次启动   默认为true
        boolean firstLoad = sharepreferences.getBoolean("firstLoad", true);
        if (firstLoad){
            splash_vp.setAdapter(new ViewPagerImageAdapter(getImgList()));
            splash_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                  if (position==mImgList.size()-1){
                      if (isOnPageChange){
                          setProcessFlag();
                          handler.sendEmptyMessageDelayed(MsgConstant.MSG_SPLASH_FIRST,2000);
                      }
                  }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //第一次启动后，firstLoad 置为false 以便以后直接进入主界面不再显示欢迎界面
            editor.putBoolean("firstLoad", false);
            //提交，执行操作
            editor.commit();
        }else {
            handler.sendEmptyMessage(MsgConstant.MSG_SPLASH_SECOND);
        }
    }

    private synchronized void setProcessFlag() {
        isOnPageChange = false;
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MsgConstant.MSG_SPLASH_FIRST:
                    if (canTrans){
                        handler.sendEmptyMessage(MsgConstant.MSG_SPLASH_SECOND);
                    }
                    break;

                case MsgConstant.MSG_SPLASH_SECOND:
                    if ( startImage!= null && !"".equals(startImage)) {
                        Glide.with(AdvertisementActivity.this).load(startImage).into(splash_start_iv);
                    }else {
                        splash_start_iv.setBackgroundResource(R.mipmap.ce_account_defult_bg);
                    }
                    splash_start_iv.setVisibility(View.VISIBLE);
                    splash_seconds_tv.setVisibility(View.VISIBLE);

                    handler.sendEmptyMessageDelayed(MsgConstant.MSG_SPLASH_TIME,1000);
                    break;
                case MsgConstant.MSG_SPLASH_TIME:
                    if (canHandler&&seconds>0){
                        seconds--;
                        splash_seconds_tv.setText("跳过" + seconds + "S");
                    }

                    if (seconds==0&&canHandler){
                        initLogin();
                    }

                    else {
                        handler.sendEmptyMessageDelayed(MsgConstant.MSG_SPLASH_TIME,1000);
                    }

                    break;
            }
        }
    };


    /**
     *  获取启动页图片  -网络请求
     */
    private void initImg(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_IMG_LIST)
                    .addParams("AdCid","Start_Page")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"图片 = "+response);
                            Gson gson=new Gson();
                            ImageBean parse=gson.fromJson(response,ImageBean.class);
                            if ("OK".equals(parse.getStatus())){
                                if (parse.getImagelist().size()>0&&parse.getImagelist()!=null){
                                    startImage=ImageUtils.reSetHeadImageURL(parse.getImagelist().get(0).getImgPath());
                                    isFirstLoadSet();
                                }else {
                                    isFirstLoadSet();
                                }
                            }
                        }
                    });
        }else {
            isFirstLoadSet();
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
                                        LyToast.shortToast(AdvertisementActivity.this,parse.getMsg());
                                        String spUserid = SpSimpleUtils.getSp("userid", AdvertisementActivity.this, "LoginActivity");
                                        if ("".equals(spUserid)){
                                            SpSimpleUtils.saveSp("userid", parse.getUserid(),AdvertisementActivity.this,"LoginActivity");
                                        }else{
                                            SpSimpleUtils.removeSharedPreference("userid",AdvertisementActivity.this,"LoginActivity");
                                            SpSimpleUtils.saveSp("userid", parse.getUserid(),AdvertisementActivity.this,"LoginActivity");
                                        }
                                        startActivity(new Intent(AdvertisementActivity.this,HomeActivity.class));
                                        finish();
                                    }else {
                                        ProgressDialogUtils.closeProgressDialog();
                                        LyToast.shortToast(AdvertisementActivity.this,parse.getMsg());
                                    }
                                }
                            });
                }else {
//             当不是自动登录的情况下
                    startActivity(new Intent(AdvertisementActivity.this,LoginActivity.class));
                    finish();

                }
            }else {
//             当没有网络的情况下
                startActivity(new Intent(AdvertisementActivity.this,LoginActivity.class));
                finish();

            }
        }catch (IllegalStateException e){
            BuglyLog.e("Wu","网络状态不对 = "+e.getMessage());
            LyToast.shortToast(this,"您的网络不稳定请稍后重新登录.....");
        }catch (NullPointerException e){
            BuglyLog.e("Wu","自动登录空指针异常 = "+e.getMessage());
        }
    }

    private List<ImageView> getImgList(){
        mImgList=new ArrayList<>();
        mImgList.add(getImageView(R.drawable.ly_activity_splash_o));
        mImgList.add(getImageView(R.drawable.ly_activity_splash_t));
        ImageView imageView = getImageView(R.drawable.ly_activity_splash_tt);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canTrans=false;
                handler.sendEmptyMessage(MsgConstant.MSG_SPLASH_SECOND);
            }
        });
        mImgList.add(imageView);
        return mImgList;
    }

    /**
     * 动态创建ImageView
     *
     * @param resId
     * @return
     */
    private ImageView getImageView(int resId) {
//        new一个对象出来
        imageView = new ImageView(this);
//        设置图片资源
        imageView.setImageResource(resId);
//        设置每张图片的样式
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//      设置没张图的尺寸大小
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(300, 300);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imageView!=null){
            ImageUtils.releaseImageViewResouce(imageView);
        }
        handler.removeCallbacksAndMessages(null);
    }
}
