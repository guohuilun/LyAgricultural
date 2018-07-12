package com.lyagricultural.yuanjian.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.gyf.barlibrary.ImmersionBar;
import com.lyagricultural.R;
import com.lyagricultural.app.LyAgriculturalApplication;
import com.tongguan.yuanjian.family.Utils.PersonManager;
import com.tongguan.yuanjian.family.Utils.service.BaseCallback;

/**
 * Created by admin on 2018/6/8.
 */
public abstract class BaseYuanJianActivity extends Activity {
    private static final String TAG = "BaseYuanJianActivity";
    protected Context baseContext;

    private ViewFlipper mContentView;
    protected View  status_bar_view;
    protected RelativeLayout mHeadLayout;
    protected TextView mTitle;
    protected RelativeLayout mRlLeft;
    protected ImageView mImageLeft;
    protected RelativeLayout mRlRight;
    protected ImageView mImageRight;
    protected RelativeLayout mRlTxRight;
    protected TextView mTxRight;
    protected ImmersionBar mImmersionBar;

    //在线视频需要的
    protected Handler handler;
    protected BaseCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.ly_layout_base);
        ((LyAgriculturalApplication) getApplication()).activityManager.pushActivity(this);
        baseContext=this;
        initView();
        handler=new Handler();
    }

    private void initView(){
//     初始化公共头部
        mContentView=super.findViewById(R.id.layout_container);
        status_bar_view=super.findViewById(R.id.status_bar_view);
        mHeadLayout=super.findViewById(R.id.layout_head);
        mTitle=super.findViewById(R.id.title_center);
        mRlLeft=super.findViewById(R.id.title_left);
        mImageLeft=super.findViewById(R.id.img_left);
        mRlRight=super.findViewById(R.id.title_right);
        mImageRight=super.findViewById(R.id.img_right);
        mRlTxRight=super.findViewById(R.id.title_right_txt);
        mTxRight=super.findViewById(R.id.txt_right);
        if (isImmersionBarEnabled()){
            setImmersionBar();
        }

    }


    @Override
    public void setContentView(View view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        mContentView.addView(view, lp);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(view);
    }

    /**
     * 设置系统返回键
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return false;
    }

    /**
     * 设置头部是否可见
     */

    public void setHeadVisibility(int visibility){
        mHeadLayout.setVisibility(visibility);
    }

    /**
     * 设置左边图标是否可见
     */

    public void setHeadLeftVisibility(int visibility){
        mRlLeft.setVisibility(visibility);
    }

    /**
     * 设置右边图标是否可见
     */

    public void setHeadRightVisibility(int visibility){
        mImageRight.setVisibility(visibility);
    }

    /**
     * 设置右边文字是否可见
     */

    public void setHeadRightTxVisibility(int visibility){
        mRlTxRight.setVisibility(visibility);
    }

    /**
     * 设置标题
     */
    public void setTitle(int titleId){
        mTitle.setText(getString(titleId));
    }

    /**
     * 设置标题
     */

    public void setTitle(String title){
        mTitle.setText(title);
    }

    /**
     * 点击左按钮
     */
    public void onHeadLeftRl(View v){
        finish();
    }

    /**
     * 点击右按钮
     */
    public void onHeadRightRl(View v){

    }

    /**
     * 设置右边文字
     */
    public void setRightTitle(String title){
        mTxRight.setText(title);
    }

    /**
     * 添加get/set方法方便调用
     * @return
     */
    public RelativeLayout getmRlLeft() {
        return mRlLeft;
    }

    public void setmRlLeft(RelativeLayout mRlLeft) {
        this.mRlLeft = mRlLeft;
    }

    public RelativeLayout getmRlRight() {
        return mRlRight;
    }

    public void setmRlRight(RelativeLayout mRlRight) {
        this.mRlRight = mRlRight;
    }

    public RelativeLayout getmRlTxRight() {
        return mRlTxRight;
    }

    public void setmRlTxRight(RelativeLayout mRlTxRight) {
        this.mRlTxRight = mRlTxRight;
    }

    public ImageView getmImageLeft() {
        return mImageLeft;
    }

    public void setmImageLeft(ImageView mImageLeft) {
        this.mImageLeft = mImageLeft;
    }

    public ImageView getmImageRight() {
        return mImageRight;
    }

    public void setmImageRight(ImageView mImageRight) {
        this.mImageRight = mImageRight;
    }

    public TextView getmTxRight() {
        return mTxRight;
    }

    public void setmTxRight(TextView mTxRight) {
        this.mTxRight = mTxRight;
    }



    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 状态栏的初始化
     */

    protected void setImmersionBar(){
        mImmersionBar = ImmersionBar.with(this);
      /*  mImmersionBar
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.ly_activity_tx_color)
                .init();*/
        mImmersionBar.statusBarView(status_bar_view).init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mImmersionBar!=null){
            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        }

        ((LyAgriculturalApplication) getApplication()).activityManager.popActivity(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PersonManager.getPersonManager().setCallback(getCallback());
    }


    protected abstract BaseCallback getCallback();


    @Override
    protected void onPause() {
        super.onPause();
        PersonManager.getPersonManager().removeCallback(callback);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }


}
