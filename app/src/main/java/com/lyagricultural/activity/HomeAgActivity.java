package com.lyagricultural.activity;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.lyagricultural.R;
import com.lyagricultural.adapter.HomeActivityAdapter;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.app.LyAgriculturalApplication;
import com.lyagricultural.customview.NoSlidingViewPager;
import com.lyagricultural.fragment.ManageFragment;
import com.lyagricultural.fragment.TaskFragment;
import com.lyagricultural.utils.GradientDrawableUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.SpSimpleUtils;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * 作者Administrator on 2018/5/29 0029 10:14
 */
public class HomeAgActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "HomeAgActivity";

    private NoSlidingViewPager home_ag_view_pager;
    private RadioButton manage_RB;
    private RadioButton task_RB;
    private RadioGroup bottom_ag_RG;
    private DrawerLayout drawerLayout;

    private RelativeLayout title_left_set;
    private ImageView img_left_set;

    private RelativeLayout drawer_layout_title_left_rl;
    private Button ce_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_home_ag);
        initView();
        setHeadVisibility(View.GONE);
    }


    private void initView(){
        home_ag_view_pager=findViewById(R.id.home_ag_view_pager);
        manage_RB=findViewById(R.id.manage_RB);
        task_RB=findViewById(R.id.task_RB);
        manage_RB.setOnClickListener(this);
        task_RB.setOnClickListener(this);
        home_ag_view_pager.setAdapter(new HomeActivityAdapter(getSupportFragmentManager(),getFragments()));
        bottom_ag_RG=findViewById(R.id.bottom_ag_RG);
        drawerLayout=findViewById(R.id.drawerLayout);
        title_left_set=findViewById(R.id.title_left_set);
        img_left_set=findViewById(R.id.img_left_set);
        title_left_set.setOnClickListener(this);
        drawer_layout_title_left_rl=findViewById(R.id.drawer_layout_title_left_rl);
        drawer_layout_title_left_rl.setOnClickListener(this);

//        测试 代码写shape
        ce_button=findViewById(R.id.ce_button);
        ce_button.setBackground(GradientDrawableUtils.setGDrawable(8,"#77C34F"));


        setNotifySp();

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_left_set:
//                drawerLayout.openDrawer(Gravity.START);
                startActivity(new Intent(HomeAgActivity.this,PersonalNotifyActivity.class));
                break;
            case R.id.drawer_layout_title_left_rl:
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.manage_RB:
                home_ag_view_pager.setCurrentItem(0);
                break;
            case R.id.task_RB:
                home_ag_view_pager.setCurrentItem(1);
                break;
        }
    }

    private List<Fragment> getFragments(){
        List<Fragment> mList=new ArrayList<>();
        mList.add(new ManageFragment());
        mList.add(new TaskFragment());
        return mList;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                LyToast.shortToast(HomeAgActivity.this,"在按一次退出程序");
                ((LyAgriculturalApplication)(getApplication())).activityManager.finishAllActivity();
                break;
        }
        //返回false是不吃掉，后面的监听也能得到这个事件，而返回true是吃掉，后面的监听就得不到这个事件了。
        return false;

    }


    private void setNotifySp(){
        if ("".equals(SpSimpleUtils.getSp("isCheckedSound", this, "PersonalNotifyActivity"))){
            LyLog.i(TAG,"你是空字符");
            setStyleBasic("sound");
        }else if ("true".equals(SpSimpleUtils.getSp("isCheckedSound", this, "PersonalNotifyActivity"))){
            LyLog.i(TAG,"你是true");
            setStyleBasic("sound");
        }else if ("false".equals(SpSimpleUtils.getSp("isCheckedSound", this, "PersonalNotifyActivity"))){
            LyLog.i(TAG,"你是false");
            setStyleBasic("shock");
        }
    }


   /* private void setNotifyStateSp(){
        LyLog.i(TAG,"你的状态 = "+SpSimpleUtils.getSp("isCheckedStatus", this, "PersonalNotifyActivity"));
        if ("".equals(SpSimpleUtils.getSp("isCheckedStatus", this, "PersonalNotifyActivity"))){
              setNotifySp();
        }else if ("true".equals(SpSimpleUtils.getSp("isCheckedStatus", this, "PersonalNotifyActivity"))){
              setNotifySp();
        }else if ("false".equals(SpSimpleUtils.getSp("isCheckedStatus", this, "PersonalNotifyActivity"))){

        }
    }*/



    /**
     * 设置通知提示方式 - 基础属性
     */
    private void setStyleBasic(String msg) {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(HomeAgActivity.this);
        builder.statusBarDrawable = R.mipmap.ly_app_logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        if ("sound".equals(msg)){
            LyLog.i(TAG,"你进来了吗0？");
            builder.notificationDefaults = Notification.DEFAULT_SOUND;
        }else if ("shock".equals(msg)){
            LyLog.i(TAG,"你进来了吗1？");
            builder.notificationDefaults = Notification.DEFAULT_VIBRATE;
        }
        //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setDefaultPushNotificationBuilder(builder);
    }

}
