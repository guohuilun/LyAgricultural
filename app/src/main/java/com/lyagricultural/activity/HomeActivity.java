package com.lyagricultural.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lyagricultural.R;
import com.lyagricultural.adapter.HomeActivityAdapter;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.app.LyAgriculturalApplication;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.customview.NoSlidingViewPager;
import com.lyagricultural.fragment.AccountFragment;
import com.lyagricultural.fragment.LandFragment;
import com.lyagricultural.fragment.ShopFragment;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.SpSimpleUtils;
import com.tongguan.yuanjian.family.Utils.PersonManager;
import com.tongguan.yuanjian.family.Utils.RequestCallback;
import com.tongguan.yuanjian.family.Utils.req.LoginRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者Administrator on 2018/5/29 0029 10:14
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "HomeActivity";
    private NoSlidingViewPager homeViewPager;
    private RadioButton landRB;
    private RadioButton shopRB;
    private RadioButton accountRB;
    private RadioGroup bottom_RG;
    private LoginRequest _loginReq =new LoginRequest();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_home);
        setHeadVisibility(View.GONE);
        initView();

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }


    private void initView(){
        homeViewPager=findViewById(R.id.home_view_pager);
        landRB=findViewById(R.id.land_RB);
        shopRB=findViewById(R.id.shop_RB);
        accountRB=findViewById(R.id.account_RB);
        landRB.setOnClickListener(this);
        shopRB.setOnClickListener(this);
        accountRB.setOnClickListener(this);
        homeViewPager.setAdapter(new HomeActivityAdapter(getSupportFragmentManager(),getFragments()));
        bottom_RG=findViewById(R.id.bottom_RG);
//        setYJLogin();
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.land_RB:
                homeViewPager.setCurrentItem(0);
                break;
            case R.id.shop_RB:
                homeViewPager.setCurrentItem(1);
                break;
            case R.id.account_RB:
                homeViewPager.setCurrentItem(2);
                break;
        }
    }

    private List<Fragment> getFragments(){
        List<Fragment> mList=new ArrayList<>();
        mList.add(new LandFragment());
        mList.add(new ShopFragment());
        mList.add(new AccountFragment());
        return mList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                LyToast.shortToast(HomeActivity.this,"在按一次退出程序");
                ((LyAgriculturalApplication)(getApplication())).activityManager.finishAllActivity();
                break;
        }
        //返回false是不吃掉，后面的监听也能得到这个事件，而返回true是吃掉，后面的监听就得不到这个事件了。
        return false;

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sentParsms(EventBusDefaultBean bean) {//此方法类似于广播，任何地方都可以传递
        if ("ShopFragment".equals(bean.getMsg())){
            homeViewPager.setCurrentItem(1);
            shopRB.setChecked(true);
            EventBus.getDefault().post(new EventBusDefaultBean("ShopLandFragmentInit"));
        }else if ("LandFragment".equals(bean.getMsg())){
            homeViewPager.setCurrentItem(0);
            landRB.setChecked(true);
            EventBus.getDefault().post(new EventBusDefaultBean("LandFragmentInit"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }

       /* LogoutRequest lr=new LogoutRequest();
        if (lr!=null){
            lr.setRequestCallback(new RequestCallback()
            {
                @Override
                public void onPostExecute(int result)
                {
                    super.onPostExecute(result);
                    LyLog.i(TAG,"执行退出登录没 = "+result);
                }
            });
            PersonManager.getPersonManager().doLogout(lr);
        }*/
    }


    /**
     * 在线视频 登录
     */
    private void setYJLogin(){
        if (Build.VERSION.SDK_INT >=16) {
            _loginReq.setServerIP("ipc.tongguantech.com");
            _loginReq.setServerPort((short) 13000);
            _loginReq.setUser("CQYCHMGJJZG2071");
            _loginReq.setPwd("CQYCHMGJJZG2071");
            _loginReq.setNodeID(123456789);
            _loginReq.setLoginType(0);
            _loginReq.setRequestCallback(new RequestCallback(){
                @Override
                public void onPostExecute(int result) {
                    LyLog.i(TAG,"在线视频登录结果 = "+result);
                    if (result!=0){
//                        int loginHandle = PersonManager.getPersonManager().getLoginHandle();
                        SpSimpleUtils.removeSharedPreference("loginHandle",HomeActivity.this,"HomeActivity");
                        SpSimpleUtils.saveSp("loginHandle",String.valueOf(result),HomeActivity.this,"HomeActivity");
                    }
                }
            });
            PersonManager.getPersonManager().doLogin(_loginReq);
        }
    }


}
