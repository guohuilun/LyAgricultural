package com.lyagricultural.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
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

import com.lyagricultural.R;
import com.lyagricultural.bean.EventBusBreedHiveBean;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.WidthUtils;
import com.lyagricultural.yuanjian.activity.BaseYuanJianActivity;
import com.lyagricultural.yuanjian.adapter.IPCListUtils;
import com.lyagricultural.yuanjian.model.CameraInfo;
import com.lyagricultural.yuanjian.model.ProjectSetting;
import com.lyagricultural.yuanjian.model.ToolUtils;
import com.tongguan.yuanjian.family.Utils.PersonManager;
import com.tongguan.yuanjian.family.Utils.PlayVideoUtil;
import com.tongguan.yuanjian.family.Utils.RequestCallback;
import com.tongguan.yuanjian.family.Utils.callback.CallBackInterface;
import com.tongguan.yuanjian.family.Utils.constant.ProtocolConstant;
import com.tongguan.yuanjian.family.Utils.gl2.VideoGlSurfaceView;
import com.tongguan.yuanjian.family.Utils.req.GetDeviceListRequest;
import com.tongguan.yuanjian.family.Utils.req.LogoutRequest;
import com.tongguan.yuanjian.family.Utils.req.StreamParams;
import com.tongguan.yuanjian.family.Utils.service.BaseCallback;
import com.tongguan.yuanjian.family.Utils.service.MainCallbackImp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 作者Administrator on 2018/5/29 0029 13:39
 */
public class BreedHiveNameMyActivity extends BaseYuanJianActivity implements View.OnClickListener{
    private static final String TAG = "BreedHiveNameMyActivity";
    private RecyclerView breed_hive_name_my_rv;
    private LinearLayout breed_hive_name_my_content_ll;
    private TextView breed_hive_name_list_number_tv;
    private TextView breed_hive_name_history_tv;
    private TextView breed_hive_name_my_time_tv;
    private TextView breed_hive_name_my_temperature_tv;
    private TextView breed_hive_name_my_humidity_tv;
    private TextView breed_hive_name_my_weight_tv;
    private TextView breed_hive_name_my_fly_out_tv;
    private TextView breed_hive_name_my_fly_in_tv;
    private ImageView breedHiveNameMyHarvestIv;

    private  PopupWindow popupWindow;

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
    private RelativeLayout play_suspensionRl;
    private ImageView mIvSuspension;

    private RelativeLayout layout_head_o;
    private TextView title_center_o;
    private RelativeLayout title_left_o;


    protected boolean isplaying = false;
    protected int playType= ProtocolConstant.PLAYREALSTRENM;
    private OnPlaybackListeners playbackCallback;
    private boolean display;   //用于是否显示其他按钮


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
        setContentView(R.layout.ly_activity_breed_hive_name_my);
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
             intent.getStringExtra("");
            setTitle("我的蜂箱");
        }
        breed_hive_name_my_rv= findViewById(R.id.breed_hive_name_my_rv);
        breed_hive_name_my_content_ll= findViewById(R.id.breed_hive_name_my_content_ll);
        breed_hive_name_list_number_tv = findViewById(R.id.breed_hive_name_list_number_tv);
        breed_hive_name_history_tv = findViewById(R.id.breed_hive_name_history_tv);
        breed_hive_name_my_time_tv=findViewById(R.id.breed_hive_name_my_time_tv);
        breed_hive_name_my_temperature_tv=findViewById(R.id.breed_hive_name_my_temperature_tv);
        breed_hive_name_my_humidity_tv=findViewById(R.id.breed_hive_name_my_humidity_tv);
        breed_hive_name_my_weight_tv=findViewById(R.id.breed_hive_name_my_weight_tv);
        breed_hive_name_my_fly_out_tv=findViewById(R.id.breed_hive_name_my_fly_out_tv);
        breed_hive_name_my_fly_in_tv=findViewById(R.id.breed_hive_name_my_fly_in_tv);
        breedHiveNameMyHarvestIv =findViewById(R.id.breedHiveNameMyHarvestIv);
        breedHiveNameMyHarvestIv.setOnClickListener(this);
        breed_hive_name_list_number_tv.setOnClickListener(this);
        breed_hive_name_history_tv.setOnClickListener(this);
        initPlay();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            视频开始监听
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
            case R.id.breedHiveNameMyHarvestIv:
                startActivity(new Intent(this,BreedHiveHarvestActivity.class));
                break;
            case R.id.breed_hive_name_list_number_tv:
                startActivity(new Intent(BreedHiveNameMyActivity.this,BreedHiveCameraListActivity.class));
                doStop();
                break;

            case R.id.breed_hive_name_history_tv:
                startActivity(new Intent(BreedHiveNameMyActivity.this,BreedHiveHistoryActivity.class));
                doStop();
                break;
        }
    }

    private void showPopupWindow() {
//         用这个方法可以改变气泡的大小

//        先创建一个view视图
        View view = LayoutInflater.from(this).inflate(R.layout.ly_activity_breed_hive_name_my_pop, null);
//        第一个参数是view，第二个参数是宽，第三个参数是高，第四个参数是是否有焦点
        int screenWidth = WidthUtils.getScreenWidth(this);
        popupWindow=new PopupWindow(view,screenWidth/3, 400,false);
//         给popupWindow设置监听事件需要通过view去得到Id来设置
        view.findViewById(R.id.breed_hive_name_pop_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(BreedHiveNameMyActivity.this,BreedHiveHistoryActivity.class));
                doStop();
                popupWindow.dismiss();
            }
        });

        view.findViewById(R.id.breed_hive_name_pop_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BreedHiveNameMyActivity.this,BreedHiveCameraListActivity.class));
                doStop();
                popupWindow.dismiss();
            }
        });
//          只要焦点设置为true那么下面这个属性是没有效果的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
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
        play_suspensionRl=findViewById(R.id.play_suspensionRl);
        mIvSuspension = (ImageView) findViewById(R.id.play_suspension);
        layout_head_o=findViewById(R.id.layout_head_o);
        title_center_o=findViewById(R.id.title_center_o);
        title_left_o=findViewById(R.id.title_left_o);
        title_center_o.setText("土地名称");

        surface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);   //不缓冲
        surface.getHolder().setKeepScreenOn(true);   //保持屏幕高亮
        play_suspensionRl.setOnClickListener(this);
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
    public void sentParsms(EventBusBreedHiveBean bean) {//此方法类似于广播，任何地方都可以传递
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
                    }else{
                        LyLog.i(TAG,"视频播放了");
                        play_stop.setEnabled(true);
                        isplaying=true;
                        loadingVideo.setVisibility(View.GONE);
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
        if(isplaying) {
            isplaying = false;
            LyLog.i(TAG,"视频暂停了");
            PlayVideoUtil.getInstance().stopPlay();
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
                    LyToast.shortToast(BreedHiveNameMyActivity.this,data.toString());
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
            breed_hive_name_my_content_ll.setVisibility(View.GONE);

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
            breed_hive_name_my_content_ll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onScreenOrientationChanged(newConfig);
    }

    /**
     * 在线视频结束
     */

}
