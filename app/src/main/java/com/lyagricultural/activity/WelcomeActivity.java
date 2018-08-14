package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lyagricultural.R;
import com.lyagricultural.adapter.ViewPagerImageAdapter;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.constant.MsgConstant;
import com.lyagricultural.utils.ImageUtils;
import com.lyagricultural.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者Administrator on 2018/7/16 0016 10:35
 */
public class WelcomeActivity extends BaseActivity{
    private static final String TAG = "WelcomeActivity";
    private ViewPager welcome_guide_vp;
    private List<ImageView> mImgList;
    private ImageView  imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_welcome_guide);
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
        welcome_guide_vp=findViewById(R.id.welcome_guide_vp);
        setVp();
    }


    private void setVp(){
        welcome_guide_vp.setAdapter(new ViewPagerImageAdapter(getImgList()));
        welcome_guide_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    private List<ImageView> getImgList(){
        mImgList=new ArrayList<>();
        mImgList.add(getImageView(R.drawable.ly_activity_splash_o));
        mImgList.add(getImageView(R.drawable.ly_activity_splash_t));
        ImageView imageView = getImageView(R.drawable.ly_activity_splash_tt);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterSplashActivity();
            }
        });
        mImgList.add(imageView);
        return mImgList;
    }

    private void enterSplashActivity() {
        Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
        startActivity(intent);
        SpUtils.putBoolean(WelcomeActivity.this, MsgConstant.FIRST_OPEN, true);
        finish();
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
    }


    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        SpUtils.putBoolean(WelcomeActivity.this, MsgConstant.FIRST_OPEN, true);
        finish();
    }
}
