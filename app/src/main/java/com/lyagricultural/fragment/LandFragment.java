package com.lyagricultural.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.activity.LandDetailsNameActivity;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.bean.ImageBean;
import com.lyagricultural.bean.LandFragmentBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.permissions.RuntimeRationale;
import com.lyagricultural.utils.BannerUtils;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.ProgressDialogUtils;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.view.TextSpan;
import com.tongguan.yuanjian.family.Utils.PersonManager;
import com.tongguan.yuanjian.family.Utils.RequestCallback;
import com.tongguan.yuanjian.family.Utils.req.LoginRequest;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/5/22 0022 12:28
 */
public class LandFragment extends Fragment{
    private static final String TAG = "LandFragment";
    private View landView;
    private RelativeLayout land_rv_rl;
    private ImageView land_iv;
    private Banner land_banner;
    private RecyclerView land_rv;
    private LinearLayout land_ll;
    private TextView land_tv;
    private  BaseRecyclerAdapter<LandFragmentBean.LaninfoBean> baseRecyclerAdapter;
    private  List<LandFragmentBean.LaninfoBean> mList;
    private String LandFragmentInit="";
    private LoginRequest _loginReq =new LoginRequest();

    private Boolean isLogin=false;
    private String landName;
    private String goodsId ;
    private String endDt ;
    private List<String> ImageGoodData=new ArrayList<>();
    private Boolean isShowLand=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        landView=inflater.inflate(R.layout.ly_fragment_land,null);
        initView();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        return landView;
    }

    private void initView(){
        land_rv_rl=landView.findViewById(R.id.land_rv_rl);
        land_iv=landView.findViewById(R.id.land_iv);
        land_banner=landView.findViewById(R.id.land_banner);
        land_rv=landView.findViewById(R.id.land_rv);
        land_ll=landView.findViewById(R.id.land_ll);
        land_tv=landView.findViewById(R.id.land_tv);
        SpannableStringBuilder spannableTv = new SpannableStringBuilder("这里空空的~蔬菜需要大地母亲的呵护~.~" +
                "赶紧去商店-土地库选择土地吧！");
        spannableTv.setSpan(new TextSpan(){
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventBusDefaultBean("ShopFragmentSwitchLand"));
            }
        },23,29, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        land_tv.setText(spannableTv);
        land_tv.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        land_rv.setLayoutManager(layoutManager);
        if (isLogin==false){
            setPermissionUtils();
        }
        if (mList!=null){
            LyLog.i(TAG,"你进来没得 =0 "+mList.size());
            if (mList.size()>0){
                setLandFragmentRv();
            }else {
                land_rv.setVisibility(View.GONE);
                land_ll.setVisibility(View.VISIBLE);
            }
        }else {
            mList=new ArrayList<>();
            initLandFragment("initView");
            setLandFragmentRv();
        }
        if (ImageGoodData.size()>0&&ImageGoodData!=null){
            setBanner();
        }else {
            initLandImg();
        }

//        setLandRv();


    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sentParsms(EventBusDefaultBean bean) {//此方法类似于广播，任何地方都可以传递
        if ("LandFragmentInit".equals(bean.getMsg())){
            LandFragmentInit=bean.getMsg();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("LandFragmentInit".equals(LandFragmentInit)){
             initLandFragment("onResume");
             setLandFragmentRv();
            LandFragmentInit="";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }


    }


    private void setPermissionUtils(){
        if (AndPermission.hasPermissions(this, Manifest.permission.READ_PHONE_STATE)){
            LyLog.i(TAG,"存在权限");
            isLogin=true;

        }else {
          requestPermission(Manifest.permission.READ_PHONE_STATE);
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

                        isLogin=true;
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        LyLog.i(TAG,"获取权限失败");
                      /*  if (AndPermission.hasAlwaysDeniedPermission(LandFragment.this, permissions)) {
                            showSettingDialog(getActivity(), permissions);
                        }*/
                    }
                })
                .start();
    }

    /**
     * Display setting dialog.
     */
    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    /**
     * Set permissions.
     */
    private void setPermission() {
        AndPermission.with(this)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        LyLog.i(TAG,"用户从设置界面返回");
                        if (AndPermission.hasPermissions(LandFragment.this, Manifest.permission.READ_PHONE_STATE)){
                            LyLog.i(TAG,"重新获取是否存在权限");
//                            setYJLogin("");
                        }else {
                            LyToast.shortToast(getActivity(),"权限未设置完成");
                        }
                    }
                })
                .start();
    }



    /**
     * 在线视频 登录
     */
    private void setYJLogin(String landCde){
        if (Build.VERSION.SDK_INT >=16) {
            _loginReq.setServerIP("ipc.tongguantech.com");
            _loginReq.setServerPort((short) 13000);
            _loginReq.setUser(landCde);
            _loginReq.setPwd(landCde);
            _loginReq.setNodeID(123456789);
            _loginReq.setLoginType(0);
            ProgressDialogUtils.showProgressDialog(getActivity(),"正在加载摄像头，请稍后...");
            _loginReq.setRequestCallback(new RequestCallback(){
                @Override
                public void onPostExecute(int result) {
                    LyLog.i(TAG,"在线视频登录结果 = "+result);
                    if (result!=0){
                        startActivity(new Intent(getActivity(), LandDetailsNameActivity.class)
                                .putExtra("landName",landName)
                                .putExtra("goodsId",goodsId)
                                .putExtra("endDt",endDt)
                        );
                        ProgressDialogUtils.closeProgressDialog();
                    }else {
                        ProgressDialogUtils.closeProgressDialog();
                        LyToast.shortToast(getActivity(),"获取摄像头失败");
                    }
                }

                @Override
                public void onPermissionDenied(String[] strings) {
                    LyLog.i(TAG,"在线视频登录权限");
                }
            });
            PersonManager.getPersonManager().doLogin(_loginReq);
        }
    }



    /**
     *  获取用户土地  -网络请求
     */
    private void initLandFragment(final String msg){
        if (CheckNetworkUtils.checkNetworkAvailable(getActivity())){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_LAND_SELECT)
                    .addParams("userId", SpSimpleUtils.getSp("userid",getActivity(),"LoginActivity"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取用户土地 = "+msg+" ... " +response);
                            Gson gson=new Gson();
                           LandFragmentBean parse=gson.fromJson(response,LandFragmentBean.class);
                           if ("OK".equals(parse.getStatus())){
                                land_ll.setVisibility(View.GONE);
                                land_rv.setVisibility(View.VISIBLE);
                                mList.clear();
                                mList.addAll(parse.getLaninfo());
                                baseRecyclerAdapter.notifyDataSetChanged();
                               isShowLand=false;
                           }else {
                               land_ll.setVisibility(View.VISIBLE);
                               land_rv.setVisibility(View.GONE);
                           }
                        }
                    });
        }
    }

    private void setLandFragmentRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandFragmentBean.LaninfoBean>(getActivity(),mList,R.layout.ly_fragment_land_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandFragmentBean.LaninfoBean laninfoBean, int position) {
                holder.setClick(R.id.land_rv_rl,laninfoBean,position,baseRecyclerAdapter);
                holder.setImg(getActivity(),laninfoBean.getLandImg(),R.id.iv_default);
                holder.setTxt(R.id.land_rv_item_name_tv,laninfoBean.getLandNme());
                if ("0".equals(laninfoBean.getIsNew())){
                    holder.setInVisibility(R.id.land_rv_item_notify_tv,View.GONE);
                }else {
                    holder.setInVisibility(R.id.land_rv_item_notify_tv,View.VISIBLE);
                    holder.setTxt(R.id.land_rv_item_notify_tv,laninfoBean.getIsNew());
                }
                holder.setTxt(R.id.land_rv_item_type_number_tv,laninfoBean.getCropType());
                holder.setTxt(R.id.land_rv_item_number_tv,laninfoBean.getCropNum());
                holder.setTxt(R.id.land_rv_item_area_tv,laninfoBean.getLandArea()+"㎡/"+laninfoBean.getUseArea()+"㎡");
                holder.setTxt(R.id.land_rv_item_term_tv,laninfoBean.getEndDt());
            }

            @Override
            public void clickEvent(int viewId, LandFragmentBean.LaninfoBean laninfoBean, int position) {
                super.clickEvent(viewId, laninfoBean, position);
                switch (viewId){
                    case R.id.land_rv_rl:
                        if (isLogin){
                            landName=laninfoBean.getLandNme();
                            goodsId=laninfoBean.getGoodsId();
                            endDt=laninfoBean.getEndDt();
//                            laninfoBean.setLandCde("CQYCHMGJJZG2071");
                            if (!"".equals(laninfoBean.getLandCde())){
                                setYJLogin(laninfoBean.getLandCde());
//                                测试时使用
//                                startActivity(new Intent(getActivity(), LandDetailsNameActivity.class)
//                                        .putExtra("landName",landName)
//                                        .putExtra("goodsId",goodsId)
//                                        .putExtra("endDt",endDt)
//                                );
                            }else {
                                LyToast.shortToast(getActivity(),"暂无视频");
                            }
                        }else {
                            LyToast.shortToast(getActivity(),"请检查权限或者网络。。。。");
                        }
                        break;
                }
            }
        };
        land_rv.setAdapter(baseRecyclerAdapter);
    }


    private void setBanner(){
        land_banner.setDelayTime(3000)
                .setImages(ImageGoodData)
                .setImageLoader(new BannerUtils.GlideImageLoader())
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
    }

    /**
     *  获取商品  -土地图片 -网络请求
     */
    private void initLandImg(){
        if (CheckNetworkUtils.checkNetworkAvailable(getActivity())){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_IMG_LIST)
                    .addParams("AdCid","Home_Index_Image")
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
                                    ImageGoodData.clear();
                                    for (int i = 0; i <parse.getImagelist().size() ; i++) {
                                        ImageGoodData.add(parse.getImagelist().get(i).getImgPath());
                                    }
                                    setBanner();
                                }
                            }
                        }
                    });
        }
    }



    /**
     * 测试数据
     */
    /*private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandFragmentBean>(getActivity(),getLandRv(),R.layout.ly_fragment_land_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandFragmentBean landFragmentBean, int position) {
                holder.setClick(R.id.land_rv_rl,landFragmentBean,position,baseRecyclerAdapter);
            }

            @Override
            public void clickEvent(int viewId, LandFragmentBean landFragmentBean, int position) {
                super.clickEvent(viewId, landFragmentBean, position);
                switch (viewId){
                    case R.id.land_rv_rl:
                        startActivity(new Intent(getActivity(), LandDetailsNameActivity.class));
                        break;
                }
            }
        };
        land_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandFragmentBean> getLandRv(){
        mList=new ArrayList<>();
        LandFragmentBean landFragmentBean=new LandFragmentBean();
        mList.add(landFragmentBean);
        return mList;
    }*/

}
