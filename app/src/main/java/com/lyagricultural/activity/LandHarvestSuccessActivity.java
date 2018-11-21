package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.EventBusLandDetailsBean;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.SpSimpleUtils;
import com.yykj.mob.share.share.SharePlatformUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 作者Administrator on 2018/7/27 0027 15:24
 */
public class LandHarvestSuccessActivity extends BaseActivity implements View.OnClickListener,PlatformActionListener {
    private static final String TAG = "LandHarvestSuccessActivity";
    private Button land_harvest_success_back_button;
    private Button land_harvest_success_seed_out_button;
    private LinearLayout land_harvest_success_qq_ll;
    private LinearLayout land_harvest_success_wei_xin_ll;
    private LinearLayout land_harvest_success_wei_xin_circle_ll;
    private String userId="";
    private String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_harvest_success);
        initView();
        setHeadVisibility(View.GONE);
    }

    private void initView(){
        land_harvest_success_back_button=findViewById(R.id.land_harvest_success_back_button);
        land_harvest_success_seed_out_button=findViewById(R.id.land_harvest_success_seed_out_button);
        land_harvest_success_qq_ll=findViewById(R.id.land_harvest_success_qq_ll);
        land_harvest_success_wei_xin_ll=findViewById(R.id.land_harvest_success_wei_xin_ll);
        land_harvest_success_wei_xin_circle_ll=findViewById(R.id.land_harvest_success_wei_xin_circle_ll);
        land_harvest_success_back_button.setOnClickListener(this);
        land_harvest_success_seed_out_button.setOnClickListener(this);
        land_harvest_success_qq_ll.setOnClickListener(this);
        land_harvest_success_wei_xin_ll.setOnClickListener(this);
        land_harvest_success_wei_xin_circle_ll.setOnClickListener(this);
         userId = SpSimpleUtils.getSp("userid", LandHarvestSuccessActivity.this, "LoginActivity");
         url = "http://113.207.26.23:22099/Share/ShareView" + "?userId=" + userId + "&shareImg=" + "http://113.207.26.23:22098/upload/Image/ShareImg/shouhuo.jpg" + "&sharType=" + "ShouHuo";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.land_harvest_success_back_button:
                finish();
                break;
            case R.id.land_harvest_success_seed_out_button:
                startActivity(new Intent(LandHarvestSuccessActivity.this,AccountSeedOutActivity.class));
                finish();
                break;
            case R.id.land_harvest_success_qq_ll:

                SharePlatformUtils.showShare(this, QQ.NAME, "分享收获", url, "http://113.207.26.23:22098/upload/Image/ShareImg/shouhuo.jpg", "炫耀一下！\n" +
                        "叮咛啷当吖，咱老百姓呐，今儿真开心哇，我的私家菜园蔬菜成熟啦！",this);
                break;
            case R.id.land_harvest_success_wei_xin_ll:
                SharePlatformUtils.showShare(this, Wechat.NAME,"分享收获", url, "http://113.207.26.23:22098/upload/Image/ShareImg/shouhuo.jpg", "炫耀一下！\n" +
                        "叮咛啷当吖，咱老百姓呐，今儿真开心哇，我的私家菜园蔬菜成熟啦！",this);
                break;
            case R.id.land_harvest_success_wei_xin_circle_ll:
                SharePlatformUtils.showShare(this, WechatMoments.NAME,"分享收获", url, "http://113.207.26.23:22098/upload/Image/ShareImg/shouhuo.jpg", "炫耀一下！\n" +
                        "叮咛啷当吖，咱老百姓呐，今儿真开心哇，我的私家菜园蔬菜成熟啦！",this);
                break;
        }
    }



    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        LyLog.i(TAG, "onComplete: "+platform+"==="+i );
        finish();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        LyLog.i(TAG, "onError: "+platform+"==="+i );
        finish();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        LyLog.i(TAG, "onCancel: "+platform+"==="+i );
        finish();
    }
}
