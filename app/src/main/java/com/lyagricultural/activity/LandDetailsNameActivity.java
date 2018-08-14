package com.lyagricultural.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.bean.EventBusLandDetailsBean;
import com.lyagricultural.bean.LandDetailsNameSelectBean;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.fragment.LandDialogFragment;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.WidthUtils;
import com.lyagricultural.view.TextSpan;
import com.lyagricultural.yuanjian.activity.BaseYuanJianActivity;
import com.lyagricultural.yuanjian.adapter.IPCListUtils;
import com.lyagricultural.yuanjian.model.CameraInfo;
import com.lyagricultural.yuanjian.model.ProjectSetting;
import com.lyagricultural.yuanjian.model.ToolUtils;
import com.tongguan.yuanjian.family.Utils.LogUtil;
import com.tongguan.yuanjian.family.Utils.PersonManager;
import com.tongguan.yuanjian.family.Utils.PlayVideoUtil;
import com.tongguan.yuanjian.family.Utils.RequestCallback;
import com.tongguan.yuanjian.family.Utils.callback.CallBackInterface;
import com.tongguan.yuanjian.family.Utils.constant.ProtocolConstant;
import com.tongguan.yuanjian.family.Utils.gl2.VideoGlSurfaceView;
import com.tongguan.yuanjian.family.Utils.req.GetDeviceListRequest;
import com.tongguan.yuanjian.family.Utils.req.LogoutRequest;
import com.tongguan.yuanjian.family.Utils.req.SnapshotRequest;
import com.tongguan.yuanjian.family.Utils.req.StreamParams;
import com.tongguan.yuanjian.family.Utils.service.BaseCallback;
import com.tongguan.yuanjian.family.Utils.service.MainCallbackImp;
import com.yykj.mob.share.share.ShareAllUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import okhttp3.Call;

/**
 * 作者Administrator on 2018/5/29 0029 13:39
 */
public class LandDetailsNameActivity extends BaseYuanJianActivity implements View.OnClickListener,PlatformActionListener{
    private static final String TAG = "LandDetailsNameActivity";
    private RecyclerView land_details_rv;
    private LinearLayout ly_land_details_weed_ll;
    private LinearLayout ly_land_details_watering_ll;
    private LinearLayout ly_land_details_fertilizer_ll;
    private LinearLayout ly_land_details_insect_pest_ll;
    private LinearLayout ly_land_details_collect_goods_ll;
    private LinearLayout land_details_rl;
    private TextView land_details_tv;
    private LinearLayout ly_land_details_content_ll;
    private LinearLayout bottom_rl;
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;
    private Boolean isCheckShow=false;
    private int isCheckShowPosition=0;
    private LandDialogFragment landDialogFragment;
    private String vegetable="";
    private int vegetableIv;
    private  PopupWindow popupWindow;
    private String landName;
    private String goodsId ;
    private String endDt ;

    private LinearLayout ly_land_details_seed_ll;

    private BaseRecyclerAdapter<LandDetailsNameSelectBean.CropinfoBean> cropinfoBeanBaseRecyclerAdapter;
    private List<LandDetailsNameSelectBean.CropinfoBean> mCropInfoBeanList;
    private List<LandDetailsNameSelectBean.CropinfoBean> mCropInfoEventBeanList=new ArrayList<>();

//     在线视频开始
    private long nid;
    private int cid;
    private boolean isOpen;
    private boolean isLive;
    private String name;
    private FrameLayout mFlLayout;
    private VideoGlSurfaceView surface;
    private ProgressBar loadingVideo;
    private RelativeLayout playStopSuspensionRl;
    private Button play_stop;
    private Button play_share;
    private RelativeLayout play_suspensionRl;
    private ImageView mIvSuspension;

    private RelativeLayout layout_head_o;
    private TextView title_center_o;
    private RelativeLayout title_left_o;


    protected boolean isplaying = false;
    protected int playType= ProtocolConstant.PLAYREALSTRENM;
    private OnPlaybackListeners playbackCallback;
    private boolean display;   //用于是否显示其他按钮

    private boolean isPause=true;


    /* 在初始化之前调用，强制竖屏 */
    private void setScreenOrietation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

//     在线视频结束

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);\
        setScreenOrietation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_details_name);
        setHeadRightVisibility(View.VISIBLE);
        mImageRight.setImageResource(R.drawable.ly_land_function);
        mImageRight.setOnClickListener(this);
        initView();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
             landName = intent.getStringExtra("landName");
             goodsId = intent.getStringExtra("goodsId");
             endDt = intent.getStringExtra("endDt");
            setTitle(landName);
        }
        land_details_rv= (RecyclerView) findViewById(R.id.land_details_rv);
        ly_land_details_weed_ll= (LinearLayout) findViewById(R.id.ly_land_details_weed_ll);
        ly_land_details_watering_ll= (LinearLayout) findViewById(R.id.ly_land_details_watering_ll);
        ly_land_details_fertilizer_ll= (LinearLayout) findViewById(R.id.ly_land_details_fertilizer_ll);
        ly_land_details_insect_pest_ll= (LinearLayout) findViewById(R.id.ly_land_details_insect_pest_ll);
        ly_land_details_collect_goods_ll= (LinearLayout) findViewById(R.id.ly_land_details_collect_goods_ll);
        land_details_rl= (LinearLayout) findViewById(R.id.land_details_rl);
        land_details_tv= (TextView) findViewById(R.id.land_details_tv);
        ly_land_details_content_ll=findViewById(R.id.ly_land_details_content_ll);
        bottom_rl=findViewById(R.id.bottom_rl);
        ly_land_details_seed_ll=findViewById(R.id.ly_land_details_seed_ll);

        initPlay();

        SpannableStringBuilder spannableDetailsTv = new SpannableStringBuilder("土地可不能空着哟~我的锄头已经准备好了！\n" +
                "赶紧去商店-种子库选择种子播种吧，绿色蔬菜我来守护！");
        spannableDetailsTv.setSpan(new TextSpan(),24,30, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        land_details_tv.setText(spannableDetailsTv);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,5);
        land_details_rv.setLayoutManager(layoutManager);
//        setLandRv();
        landDialogFragment=new LandDialogFragment();
        ly_land_details_weed_ll.setOnClickListener(this);
        ly_land_details_watering_ll.setOnClickListener(this);
        ly_land_details_fertilizer_ll.setOnClickListener(this);
        ly_land_details_insect_pest_ll.setOnClickListener(this);
        ly_land_details_collect_goods_ll.setOnClickListener(this);
        ly_land_details_seed_ll.setOnClickListener(this);

        setMLandRv();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            视频开始监听
            case R.id.play_share:
                ShareAllUtils.showShare(LandDetailsNameActivity.this,
                        "", "", "", "", this);
                isPause=false;
//                cut();
                break;
            case R.id.play_suspensionRl:
                  /* 横屏监听 */
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    layout_head_o.setVisibility(View.GONE);
                }
                 /* 竖屏监听 */
                else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    layout_head_o.setVisibility(View.GONE);
                }
                break;

            case R.id.title_left_o:
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    layout_head_o.setVisibility(View.GONE);
                }
                break;

            case R.id.play_stop:
                if(!isplaying) {
                    LyToast.shortToast(this,"正在获取视频流，请稍后。。。");
                    doplay();
                    play_stop.setBackgroundResource(R.drawable.ly_activity_land_details_name_play);
                }else {
                    doStop();
                    play_stop.setBackgroundResource(R.drawable.ly_activity_land_details_name_pause);
                }
                break;
            case R.id.surface:
                if (display) {
                    layout_head_o.setVisibility(View.GONE);
                    playStopSuspensionRl.setVisibility(View.GONE);
                    display = false;
                } else {
                    if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        layout_head_o.setVisibility(View.VISIBLE);
                    }else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                        layout_head_o.setVisibility(View.GONE);
                    }
                    playStopSuspensionRl.setVisibility(View.VISIBLE);
                    display = true;
                }
                break;


//            视频结束监听

            case R.id.img_right:
              showPopupWindow();
                break;
            case R.id.ly_land_details_weed_ll:
                if ("".equals(vegetable)){
                    LyToast.shortToast(this,"请选择某项土地作物");
                    return;
                }
                Bundle bundle=new Bundle();
                bundle.putString("vegetable",vegetable);
                bundle.putInt("vegetableIv",vegetableIv);
                landDialogFragment.setArguments(bundle);
                landDialogFragment.show(getFragmentManager(),"LandDialogFragmentWeed");
                break;
            case R.id.ly_land_details_watering_ll:
//                landDialogFragment.show(getFragmentManager(),"LandDialogFragmentWater");
                break;
            case R.id.ly_land_details_fertilizer_ll:
                break;
            case R.id.ly_land_details_insect_pest_ll:
                break;
            case R.id.ly_land_details_collect_goods_ll:
//                if ("".equals(vegetable)){
//                    LyToast.shortToast(this,"请选择某项土地作物");
//                    return;
//                }
//                Bundle bundleOne=new Bundle();
//                bundleOne.putString("vegetable",vegetable);
//                bundleOne.putInt("vegetableIv",vegetableIv);
//                landDialogFragment.setArguments(bundleOne);
//                landDialogFragment.show(getFragmentManager(),"LandDialogFragmentWeed");
                mCropInfoEventBeanList.clear();
                for (int i = 0; i <mCropInfoBeanList.size() ; i++) {
                    if (mCropInfoBeanList.get(i).getShow()){
                        mCropInfoEventBeanList.add(mCropInfoBeanList.get(i));
                    }
                }
                LyLog.i(TAG,"这里面的数量是多少 = "+mCropInfoEventBeanList.size());
                if (mCropInfoEventBeanList.size()==0){
                    LyToast.shortToast(this,"请选择某项土地作物");
                    return;
                }else {
                    EventBus.getDefault().postSticky(mCropInfoEventBeanList);
                    startActivity(new Intent(LandDetailsNameActivity.this,LandHarvestActivity.class));
                    doStop();
                }
                break;
            case R.id.ly_land_details_seed_ll:
                EventBus.getDefault().post(new EventBusDefaultBean("ShopFragmentSwitch"));
                finish();
                break;
        }
    }

    private void showPopupWindow() {
//         用这个方法可以改变气泡的大小

//        先创建一个view视图
        View view = LayoutInflater.from(this).inflate(R.layout.ly_activity_land_details_name_pop, null);
//        第一个参数是view，第二个参数是宽，第三个参数是高，第四个参数是是否有焦点
        int screenWidth = WidthUtils.getScreenWidth(this);
        popupWindow=new PopupWindow(view,screenWidth/3, 400,false);
//         给popupWindow设置监听事件需要通过view去得到Id来设置
        view.findViewById(R.id.land_details_name_pop_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandDetailsNameActivity.this,LandHistoryActivity.class)
                    .putExtra("goodsId",goodsId)
                );
                doStop();
                popupWindow.dismiss();
            }
        });

        view.findViewById(R.id.land_details_name_pop_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandDetailsNameActivity.this,LandOperationGuideActivity.class));
                doStop();
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.land_details_name_pop_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandDetailsNameActivity.this,LandCameraListActivity.class));
                doStop();
                popupWindow.dismiss();
            }
        });

        view.findViewById(R.id.land_details_name_pop_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandDetailsNameActivity.this,LandDetailsActivity.class)
                   .putExtra("goodsId",goodsId)
                   .putExtra("endDt",endDt)
                );
                doStop();
                popupWindow.dismiss();
            }
        });
//          只要焦点设置为true那么下面这个属性是没有效果的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
//         触摸事件监听
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;

                    case MotionEvent.ACTION_UP:
//                        松开的时候
                        break;

                    case  MotionEvent.ACTION_MOVE:
//                        大概没50ms获取一次坐标  触摸移动的时候
                        break;
                    case  MotionEvent.ACTION_CANCEL:
//                         触摸取消
                        break;
                }
                return false;
            }
        });


        /**
         * 设置气泡的动画效果
         */
//        popupWindow.setAnimationStyle(R.style.MyPopupWindow);
//         最后一步       设置气泡显示为下拉
        popupWindow.showAsDropDown(mRlRight);

    }




    /**
     * 在线视频开始
     */

    private void initPlay(){
        //        在线视频获取列表
        GetDeviceListRequest gdlr = new GetDeviceListRequest();
//		gdlr.setLoadingDialog(LoadingDialog.getInstance(DevicesActivity.this));
        PersonManager.getPersonManager().doGetDeviceList(gdlr);
        mFlLayout = (FrameLayout) findViewById(R.id.play_video_surface_layout);
        surface=findViewById(R.id.surface);
        loadingVideo=findViewById(R.id.loadingVideo);
        playStopSuspensionRl=findViewById(R.id.playStopSuspensionRl);
        play_stop=findViewById(R.id.play_stop);
        play_share=findViewById(R.id.play_share);
        play_suspensionRl=findViewById(R.id.play_suspensionRl);
        mIvSuspension = (ImageView) findViewById(R.id.play_suspension);
        layout_head_o=findViewById(R.id.layout_head_o);
        title_center_o=findViewById(R.id.title_center_o);
        title_left_o=findViewById(R.id.title_left_o);
        title_center_o.setText(landName);
        surface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);   //不缓冲
        surface.getHolder().setKeepScreenOn(true);   //保持屏幕高亮
        play_share.setOnClickListener(this);
        play_suspensionRl.setOnClickListener(this);
        title_left_o.setOnClickListener(this);
        play_stop.setOnClickListener(this);
        surface.setOnClickListener(this);


        surface.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                surface.onGesture(event);

                return false;
            }
        });

        // 创建录制录像的保存路径和图片保存的路径
        ToolUtils.mkDirs(this, Environment.getExternalStorageDirectory().getAbsolutePath() + ProjectSetting.SAVE_PIC_FOLDER);
        ToolUtils.mkDirs(this,Environment.getExternalStorageDirectory().getAbsolutePath() + ProjectSetting.SAVE_VIDEO_FOLDER);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayVideoUtil.getInstance().stopPlay();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }

        LogoutRequest lr=new LogoutRequest();
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
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sentParsms(EventBusLandDetailsBean bean) {//此方法类似于广播，任何地方都可以传递
       if ("OK".equals(bean.getMsgLand())){
           doplay();
       }else if ("YES".equals(bean.getMsgLand())){
           nid=bean.getNid();
           cid=bean.getCid();
           isOpen = bean.isOpen();
           isLive = bean.isLive();
           name = bean.getName();
           doplay();
           setTitle(name);
       }else if ("seedOut".equals(bean.getMsgLand())){
           finish();
       }

    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    layout_head_o.setVisibility(View.GONE);
                }else {
                    finish();
                }
                break;
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 尺寸dip
     * @return 像素值
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    //	停止的方法
    private void doStop() {
        if(isplaying) {
            isplaying = false;
            LyLog.i(TAG,"视频停止了");
            PlayVideoUtil.getInstance().stopPlay();
        }
    }

    int resolution = 2;
    //	  开始读取流  通过上传参数调用	视频流工具类：PlayVideoUtil借口去获取回调结果
    private void startRealStream() {
        if(ToolUtils.isNetworkAvailable(this)) {
//			StreamParams  播放视频统一参数，根据不同参数播放不同的视频
            StreamParams params = new StreamParams();
//			int Playtype:播放类型 0-实时视频 1-历史录像(时间方式) 2-云存储录像(文件方式) 3-历史录像(文件方式)
//			long NodeId 节点ID:
//			int chanelId 通道ID
//			int  fileId 文件ID
//			int recordType 录像类型1 - AutoRecord, 2 - 摘要录像, 3 –报警录像
//			String startTime 开始时间 格式 :”2010-03-25 09:42:54”
//			String endTime 结束时间 同上
            params.setNid(nid);
            LyLog.i(TAG,"StreamParams  nid = "+nid );
            params.setCid(cid);
            LyLog.i(TAG,"StreamParams  cid = "+cid );
            params.setResolution(resolution);
            params.setPlayType(playType);
//           CallBackInterface：视频回调
            PlayVideoUtil.getInstance().requestStream(params,new CallBackInterface() {
                //			需实现callBack 方法用以判断播放成功失败等状态（具体状态码见附表）
                @Override
                public void callBack(int result) {
                    if(result == 0) {
                        LyLog.i(TAG,"视频播放失败");
                        isplaying=false;
                        LyToast.shortToast(LandDetailsNameActivity.this,"视频播放失败。。。");
                    }else{
                        LyLog.i(TAG,"视频播放了");
                        play_stop.setEnabled(true);
                        isplaying=true;
                        loadingVideo.setVisibility(View.GONE);
                        play_stop.setVisibility(View.GONE);
                    }
                }
                //			需实现callBackProgress方法 用以获取播放时长进度等信息。
                @Override
                public void callBackProgress(String key, int value) {
                    if(key.equals(ProtocolConstant.NOTIFY_PLAY_TIME)) {
                        //play progress
                    } else if(key.equals(ProtocolConstant.NOTIFY_FILE_TIME)) {
                        //video file total time
                    }
                }
            },surface);
        } else {
            Toast.makeText(this,"请检查网络设置",Toast.LENGTH_SHORT).show();
        }
    }


    //   播放方法 播放类型
    protected void doplay() {
        isplaying = true;
        switch (playType) {
//			ProtocolConstants  定义SDK中使用的一些常量
            case ProtocolConstant.PLAYCLOUDSTREAM:
                if(playbackCallback != null)
                    playbackCallback.onPlayBack();
                break;
            case ProtocolConstant.PLAYVODSTRENM:
                if(playbackCallback != null)
                    playbackCallback.onPlayBack();
                break;
            default:
                startRealStream();
                break;
        }
    }


    //	 暂停
    @Override
    protected void onPause() {

        super.onPause();
        if (isPause){
            if(isplaying) {
                isplaying = false;
                LyLog.i(TAG,"视频暂停了");
                PlayVideoUtil.getInstance().stopPlay();
                play_stop.setVisibility(View.VISIBLE);
                play_stop.setBackgroundResource(R.drawable.ly_activity_land_details_name_pause);
            }
        }else {
            isPause=true;
        }
    }


    public OnPlaybackListeners getPlaybackCallback() {
        return playbackCallback;
    }

    //	 设置播放返回的回调借口
    public void setPlaybackCallback(OnPlaybackListeners playbackCallback) {
        this.playbackCallback = playbackCallback;
    }

    public interface OnPlaybackListeners {
        /**
         * @description 回放播放接口
         */
        void onPlayBack();
    }



    @Override
    protected BaseCallback getCallback() {
        return mainCallbackImp;
    }

    MainCallbackImp mainCallbackImp=new MainCallbackImp(){

        @Override
        public void onError(int errcode, int msgtype) {
            super.onError(errcode,msgtype);
        }

        @Override
        public void onGetDeviceList(JSONObject data) {
            IPCListUtils.parseLoginJson(data);
            ArrayList<CameraInfo> cameraList = IPCListUtils.getCameraList();
            if (cameraList.size()>0){
                nid=cameraList.get(0).getId();
                isLive=cameraList.get(0).isOnline();
                isOpen=cameraList.get(0).isOpen();
                cid=cameraList.get(0).getCid();
                name=cameraList.get(0).getName();
                LyLog.i(TAG,"nid = "+nid);
                LyLog.i(TAG,"cid = "+cid);
                LyLog.i(TAG,"name = "+name);
                LyLog.i(TAG,"是否在线 = "+isLive);
                if (isLive){
                    startRealStream();
                }
            }
        }

        @Override
        public void onGetHeatPictureUrl(final JSONObject data) {
            handler.post(new Runnable()
            {
                @Override
                public void run() {
                    LyToast.shortToast(LandDetailsNameActivity.this,data.toString());
                }
            });
        }
    };


    /**
     * 当屏幕方向发生改变时
     *
     * @param config
     */
    private void onScreenOrientationChanged(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            //隐藏状态栏

//             获取窗口属性管理
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
//             窗口的属性设置
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            ViewGroup.LayoutParams lp = mFlLayout.getLayoutParams();
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mFlLayout.setLayoutParams(lp);

            status_bar_view.setVisibility(View.GONE);
            mIvSuspension.setImageResource(R.drawable.ly_activity_land_details_name_suspension_on);
            setHeadVisibility(View.GONE);
            bottom_rl.setVisibility(View.GONE);
            ly_land_details_content_ll.setVisibility(View.GONE);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            //显示状态栏
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            ViewGroup.LayoutParams lp = mFlLayout.getLayoutParams();
            lp.height = dip2px(240);
            mFlLayout.setLayoutParams(lp);

            status_bar_view.setVisibility(View.VISIBLE);
            mIvSuspension.setImageResource(R.drawable.ly_activity_land_details_name_suspension);
            setHeadVisibility(View.VISIBLE);
            bottom_rl.setVisibility(View.VISIBLE);
            ly_land_details_content_ll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onScreenOrientationChanged(newConfig);
    }

    String imagePath = "";
    public void cut()
    {
        SnapshotRequest sr = new SnapshotRequest();

        String pathFile = Environment.getExternalStorageDirectory().getAbsolutePath() + ProjectSetting.SAVE_PIC_FOLDER;
//        imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + ProjectSetting.SAVE_PIC_FOLDER + "/" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
//                + ProjectSetting.IMAGE_SUFFIX;
        File dir = new File(pathFile);
        if (!dir.exists()) {
            if(!dir.mkdirs())
            {
                LogUtil.d("创建目录不成功");
            }
        }
        imagePath = pathFile + "/" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
                + ProjectSetting.IMAGE_SUFFIX;

        sr.setFullFileName(imagePath);
        sr.setPlayType(playType);
        sr.setRequestCallback(new RequestCallback()
        {
            @Override
            public void onPostExecute(int result)
            {
                super.onPostExecute(result);
                if (result == 0)
                {
                    LyLog.i(TAG,"截图成功 = "+imagePath);
                    Toast.makeText(LandDetailsNameActivity.this, "success imagePath: " + imagePath, Toast.LENGTH_LONG).show();

                } else
                {
                    LyLog.i(TAG,"截图失败");
                    Toast.makeText(LandDetailsNameActivity.this, "screenshot fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
        PersonManager.getPersonManager().doSnapshot(sr);
    }


    /**
     * 在线视频结束
     */


    @Override
    protected void onResume() {
        super.onResume();
        initLandCropSelect();
//        setMLandRv();
    }

    /**
     *  获取土地作物   -网络请求
     */
    private void initLandCropSelect(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_LAND_CROP_SELECT)
                    .addParams("landId", goodsId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取土地作物 = " +response);
                            Gson gson=new Gson();
                            LandDetailsNameSelectBean parse=gson.fromJson(response,LandDetailsNameSelectBean.class);
                            if ("OK".equals(parse.getStatus())) {
                                mCropInfoBeanList.clear();
                                mCropInfoBeanList.addAll(parse.getCropinfo());
                                cropinfoBeanBaseRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }


    private void setMLandRv(){
        mCropInfoBeanList=new ArrayList<>();
        cropinfoBeanBaseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameSelectBean.CropinfoBean>
                (this,mCropInfoBeanList,R.layout.ly_activity_land_details_name_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameSelectBean.CropinfoBean cropinfoBean, int position) {
                holder.setClick(R.id.land_details_name_ll,cropinfoBean,position,cropinfoBeanBaseRecyclerAdapter);
                holder.setImg(LandDetailsNameActivity.this,cropinfoBean.getCropImg(),R.id.land_details_name_rv_iv);
                holder.setTxt(R.id.land_details_name_rv_tv,cropinfoBean.getCropNme()+"("+cropinfoBean.getCropNum()+")");
                holder.setTxt(R.id.land_details_name_rv_tv_o,cropinfoBean.getCropType());

                if (!isCheckShow){
                    holder.setCheck(R.id.land_details_name_rv_cb,false);
                }else {
                    if (cropinfoBean.getShow()){
                        holder.setCheck(R.id.land_details_name_rv_cb,true);
                        cropinfoBean.setShow(true);
                    }else {
                        holder.setCheck(R.id.land_details_name_rv_cb,false);
                        cropinfoBean.setShow(false);
                    }
                }
            }

            @Override
            public void clickEvent(int viewId, LandDetailsNameSelectBean.CropinfoBean cropinfoBean, int position) {
                super.clickEvent(viewId, cropinfoBean, position);
                switch (viewId){
                    case R.id.land_details_name_ll:
                        isCheckShow=true;
                        cropinfoBean.setShow(!cropinfoBean.getShow());
                        notifyDataSetChanged();
                        break;
                }
            }
        };

        land_details_rv.setAdapter(cropinfoBeanBaseRecyclerAdapter);
    }
//    分享回调监听开始

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
     LyLog.i(TAG,"分享成功");

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        LyLog.i(TAG,"分享错误");

    }

    @Override
    public void onCancel(Platform platform, int i) {
        LyLog.i(TAG,"分享取消");

    }
//    分享回调监听

    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_land_details_name_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landDetailsNameBean, int position) {
                holder.setClick(R.id.land_details_name_ll,landDetailsNameBean,position,baseRecyclerAdapter);
                holder.setBgResource(R.id.land_details_name_rv_iv,landDetailsNameBean.getLand_details_name_rv_iv());
                holder.setTxt(R.id.land_details_name_rv_tv,landDetailsNameBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.land_details_name_rv_tv_o,landDetailsNameBean.getLand_details_name_rv_tv_o());
                holder.setBgResource(R.id.land_details_name_rv_o_iv,landDetailsNameBean.getLand_details_name_rv_o_iv());

                if (!isCheckShow){
                    holder.setCheck(R.id.land_details_name_rv_cb,false);
                }else {
                    if (landDetailsNameBean.getShow()){
                        holder.setCheck(R.id.land_details_name_rv_cb,true);
                        vegetable=landDetailsNameBean.getLand_details_name_rv_tv();
                        vegetableIv=landDetailsNameBean.getLand_details_name_rv_iv();
                        landDetailsNameBean.setShow(true);
                    }else {
                        holder.setCheck(R.id.land_details_name_rv_cb,false);
                        vegetable="";
                        landDetailsNameBean.setShow(false);
                    }
                   /* if (isCheckShowPosition==position){
                        if (landDetailsNameBean.getShow()){
                            holder.setCheck(R.id.land_details_name_rv_cb,true);
                            vegetable=landDetailsNameBean.getLand_details_name_rv_tv();
                            vegetableIv=landDetailsNameBean.getLand_details_name_rv_iv();
                            landDetailsNameBean.setShow(true);
                        }else {
                            holder.setCheck(R.id.land_details_name_rv_cb,false);
                            vegetable="";
                            landDetailsNameBean.setShow(false);
                        }
                    }else {
                        landDetailsNameBean.setShow(false);
                        holder.setCheck(R.id.land_details_name_rv_cb,false);
                    }*/
                }


            }

            @Override
            public void clickEvent(int viewId, LandDetailsNameBean landDetailsNameBean, int position) {
                super.clickEvent(viewId, landDetailsNameBean, position);
                switch (viewId){
                    case R.id.land_details_name_ll:
                        isCheckShow=true;
//                        isCheckShowPosition=position;
                        landDetailsNameBean.setShow(!landDetailsNameBean.getShow());
                        notifyDataSetChanged();
                        break;
                }
            }
        };
        land_details_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_land_details_name_s,"番茄(10)","除虫",R.mipmap.ce_land_details_name_state_o,false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_land_details_name_o,"萝卜(8)","干旱",R.mipmap.ce_land_details_name_state_t,false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_land_details_name_t,"黄瓜(2)","健康",R.mipmap.ce_land_details_name_state_tt,false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_land_details_name_tt,"玉米(1)","杂草",R.mipmap.ce_land_details_name_state_f,false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_land_details_name_ff,"土豆(5)","虫害",R.mipmap.ce_land_details_name_state_o,false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_land_details_name_f,"瘸子(4)","健康",R.mipmap.ce_land_details_name_state_tt,false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_land_details_name_o,"萝卜(8)","干旱",R.mipmap.ce_land_details_name_state_t,false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_land_details_name_t,"黄瓜(2)","健康",R.mipmap.ce_land_details_name_state_tt,false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_land_details_name_tt,"玉米(1)","杂草",R.mipmap.ce_land_details_name_state_f,false);
        mList.add(landDetailsNameBean);
        landDetailsNameBean=new LandDetailsNameBean(R.mipmap.ce_land_details_name_ff,"土豆(5)","虫害",R.mipmap.ce_land_details_name_state_o,false);
        mList.add(landDetailsNameBean);
        return mList;
    }

}
