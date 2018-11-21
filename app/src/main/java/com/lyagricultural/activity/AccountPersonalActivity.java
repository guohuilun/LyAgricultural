package com.lyagricultural.activity;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.app.LyAgriculturalApplication;
import com.lyagricultural.bean.AccountPersonalBean;
import com.lyagricultural.bean.DefaultBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.dialog.CommomDialog;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.ImageUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.utils.TxUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/6/5 0005 14:08
 */
public class AccountPersonalActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AccountPersonalActivity";
    private RelativeLayout account_personal_head_rl;
    private RelativeLayout account_personal_name_rl;
    private TextView account_personal_name_tv;
    private RelativeLayout account_personal_sex_rl;
    private TextView account_personal_sex_tv;
    private RelativeLayout account_personal_phone_rl;
    private TextView account_personal_phone_tv;
    private RelativeLayout account_personal_wei_xin_rl;
    private TextView account_personal_wei_xin_tv;
    private TextView account_personal_code_tv;
    private RelativeLayout bottom_rl;
    private ImageView account_personal_head_more_iv;
    private Bitmap bitmap;
    private RelativeLayout account_personal_people_rl;
    private TextView account_personal_people_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_account_personal);
        setTitle("我的信息");
        initView();
    }

    private void initView(){
        account_personal_head_rl=findViewById(R.id.account_personal_head_rl);
        account_personal_name_rl=findViewById(R.id.account_personal_name_rl);
        account_personal_name_tv=findViewById(R.id.account_personal_name_tv);
        account_personal_sex_rl=findViewById(R.id.account_personal_sex_rl);
        account_personal_sex_tv=findViewById(R.id.account_personal_sex_tv);
        account_personal_phone_rl=findViewById(R.id.account_personal_phone_rl);
        account_personal_phone_tv=findViewById(R.id.account_personal_phone_tv);
        account_personal_wei_xin_rl=findViewById(R.id.account_personal_wei_xin_rl);
        account_personal_wei_xin_tv=findViewById(R.id.account_personal_wei_xin_tv);
        account_personal_code_tv=findViewById(R.id.account_personal_code_tv);
        bottom_rl=findViewById(R.id.bottom_rl);
        account_personal_head_more_iv=findViewById(R.id.account_personal_head_more_iv);
        account_personal_people_rl=findViewById(R.id.account_personal_people_rl);
        account_personal_people_tv=findViewById(R.id.account_personal_people_tv);
        account_personal_head_rl.setOnClickListener(this);
        bottom_rl.setOnClickListener(this);
        account_personal_people_rl.setOnClickListener(this);
        initUserInfoSelect();
        account_personal_code_tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cm =(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(account_personal_code_tv.getText().toString());
                LyToast.shortToast(AccountPersonalActivity.this,"已复制");
                //ToastUtil.toastSth(getContext() , "订单号已复制到剪切板，快去粘贴吧~");
                return false;
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_personal_head_rl:
                 setHead();
                break;
            case R.id.bottom_rl:
                new CommomDialog(AccountPersonalActivity.this, R.style.dialog, "是否退出", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm){
                            String auto = SpSimpleUtils.getSp("auto", AccountPersonalActivity.this, "LoginActivity");
                            if (!TxUtils.TextIsEmpty(auto)&& "自动登录".equals(auto)) {
                                SpSimpleUtils.removeSharedPreference("auto",AccountPersonalActivity.this,"LoginActivity");
                                startActivity(new Intent(AccountPersonalActivity.this,LoginActivity.class));
                                ((LyAgriculturalApplication) (getApplication())).activityManager.clearAllActivityExceptTarget(LoginActivity.class);
                            }else {
                                ((LyAgriculturalApplication) (getApplication())).activityManager.popAllActivityExceptTarget(LoginActivity.class);
                            }
                            dialog.dismiss();
                        }
                    }
                }).setTitle("提示").show();

                break;
            case R.id.account_personal_people_rl:
               startActivity(new Intent(AccountPersonalActivity.this,AccountPersonalInvitationActivity.class));
                break;
        }
    }

    private void setHead(){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .isCamera(true)
                .compress(true)
                .isZoomAnim(true)
                .synOrAsy(true)
                .minimumCompressSize(100)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    for (int i = 0; i <selectList.size() ; i++) {
                        if (selectList.get(i).isCompressed()){
                            LyLog.i(TAG,"图片地址 = "+selectList.get(i).getCompressPath());
                            bitmap = BitmapFactory.decodeFile(selectList.get(i).getCompressPath());
                            account_personal_head_more_iv.setImageBitmap(bitmap);
                            initImgUpdate(selectList.get(i).getCompressPath());
                        }

                    }
                    break;
            }
        }
    }


    /**
     *  修改用户头像   -网络请求
     */
    private void initImgUpdate(String path){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_IMG_UPDATE)
                    .addParams("UserId", SpSimpleUtils.getSp("userid",AccountPersonalActivity.this,"LoginActivity"))
                    .addFile("imagePath","personHead.jpg",new File(path))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"修改用户头像 = " +response);
                            Gson gson=new Gson();
                            DefaultBean parse=gson.fromJson(response,DefaultBean.class);
                            if ("OK".equals(parse.getStatus())){
                                LyToast.shortToast(AccountPersonalActivity.this,parse.getMsg());
                                //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
                                PictureFileUtils.deleteCacheDirFile(AccountPersonalActivity.this);
                            }
                        }
                    });
        }
    }


    /**
     *  获取用户信息   -网络请求
     */
    private void initUserInfoSelect(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_INFO_SELECT)
                    .addParams("userId", SpSimpleUtils.getSp("userid",this,"LoginActivity"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取用户信息 = " +response);
                            Gson gson=new Gson();
                            AccountPersonalBean parse=gson.fromJson(response,AccountPersonalBean.class);
                            if ("OK".equals(parse.getStatus())){
                                if (parse.getUserinfo().getImagePath()!=null&&!"".equals(parse.getUserinfo().getImagePath())){
                                    String headImageURL = ImageUtils.reSetHeadImageURL(parse.getUserinfo().getImagePath());
                                    Glide.with(AccountPersonalActivity.this).load(headImageURL).into(account_personal_head_more_iv);
                                }
                                account_personal_name_tv.setText(parse.getUserinfo().getUserNme());
                                account_personal_sex_tv.setText(parse.getUserinfo().getUserSex());
                                account_personal_phone_tv.setText(parse.getUserinfo().getLoginPhone());
                                if ("".equals(parse.getUserinfo().getLoginWx())){
                                    account_personal_wei_xin_tv.setText("暂未绑定微信账号");
                                }else {
                                    account_personal_wei_xin_tv.setText(parse.getUserinfo().getLoginWx());
                                }
                                account_personal_code_tv.setText(parse.getUserinfo().getInCode());

                                account_personal_people_tv.setText(parse.getUserinfo().getInCount());
                            }

                        }
                    });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
    }
}
