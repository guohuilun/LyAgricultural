package com.lyagricultural.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.bean.EventBusLandDetailsBean;
import com.lyagricultural.cebean.LandFragmentBean;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.yuanjian.activity.BaseYuanJianActivity;
import com.lyagricultural.yuanjian.adapter.IPCListUtils;
import com.lyagricultural.yuanjian.model.CameraInfo;
import com.lyagricultural.yuanjian.util.PinyinComparator;
import com.lyagricultural.yuanjian.util.PinyinUtils;
import com.tongguan.yuanjian.family.Utils.PersonManager;
import com.tongguan.yuanjian.family.Utils.req.GetDeviceListRequest;
import com.tongguan.yuanjian.family.Utils.service.BaseCallback;
import com.tongguan.yuanjian.family.Utils.service.MainCallbackImp;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者Administrator on 2018/6/5 0005 17:15
 */
public class LandCameraListActivity extends BaseYuanJianActivity {
    private static final String TAG = "LandCameraListActivity";
    private RecyclerView land_camera_list_rv;
    private  BaseRecyclerAdapter<LandFragmentBean> baseRecyclerAdapter;
    private  List<LandFragmentBean> mList;

    private List<CameraInfo> mCameraList;
    private BaseRecyclerAdapter<CameraInfo> cameraInfoBaseRecyclerAdapter;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private Boolean isClick=false;
    private int isClickPos=0;
    private long nid;
    private int cid;
    private boolean isOpen;
    private boolean isLive;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_camera_list);
        setTitle("摄像头列表");
        initView();
    }

    private void initView(){
        land_camera_list_rv=findViewById(R.id.land_camera_list_rv);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        land_camera_list_rv.setLayoutManager(layoutManager);
        pinyinComparator = new PinyinComparator();
//        setLandRv();
//        在线视频获取列表
        GetDeviceListRequest gdlr = new GetDeviceListRequest();
//		gdlr.setLoadingDialog(LoadingDialog.getInstance(DevicesActivity.this));
        PersonManager.getPersonManager().doGetDeviceList(gdlr);
        setMCameraList();
    }


    @Override
    protected BaseCallback getCallback() {
        return mainCallbackImp;
    }

    MainCallbackImp mainCallbackImp=new MainCallbackImp(){

        @Override
        public void onError(int errcode, int msgtype) {
            LyLog.e(TAG,"视频列表参数错误 = "+errcode);
        }

        @Override
        public void onGetDeviceList(JSONObject data) {
            mCameraList.clear();
            IPCListUtils.parseLoginJson(data);
            LyLog.i(TAG,"视频列表参数 = "+data);
            ArrayList<CameraInfo> cameraList = IPCListUtils.getCameraList();
       /*     for (int i = 0; i <cameraList.size() ; i++) {
                String pinyin = PinyinUtils.ccs2Pinyin(cameraList.get(i).getName());
                cameraList.get(i).setPinying(pinyin);
            }
            Collections.sort(cameraList, pinyinComparator);*/
            mCameraList.addAll(cameraList);

            handler.sendEmptyMessage(1);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case 1:
                    cameraInfoBaseRecyclerAdapter.notifyDataSetChanged();
                    break;
            }
        };
    };


    private void setMCameraList(){
        mCameraList=new ArrayList<>();
        cameraInfoBaseRecyclerAdapter=new BaseRecyclerAdapter<CameraInfo>(this,mCameraList,R.layout.ly_activity_land_camera_list_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, CameraInfo cameraInfo, int position) {
                   if (isClick==false){
                       if (position==0){
                           holder.setInVisibility(R.id.land_camera_list_rv_item_o_tv, View.VISIBLE);
                       }else {
                           holder.setInVisibility(R.id.land_camera_list_rv_item_o_tv, View.GONE);
                       }
                   }else {
                       if (isClickPos==position){
                           holder.setInVisibility(R.id.land_camera_list_rv_item_o_tv, View.VISIBLE);
                       }else {
                           holder.setInVisibility(R.id.land_camera_list_rv_item_o_tv, View.GONE);
                       }
                   }

                   holder.setTxt(R.id.land_camera_list_rv_item_tv,cameraInfo.getName());
                   holder.setClick(R.id.land_camera_list_rv_item_rl,cameraInfo,position,cameraInfoBaseRecyclerAdapter);
            }

            @Override
            public void clickEvent(int viewId, CameraInfo cameraInfo, int position) {
                super.clickEvent(viewId, cameraInfo, position);
                switch (viewId){
                    case R.id.land_camera_list_rv_item_rl:
                         isClick=true;
                         isClickPos=position;
                         nid=cameraInfo.getId();
                         cid=cameraInfo.getCid();
                         isOpen=cameraInfo.isOpen();
                         isLive=cameraInfo.isOnline();
                         name=cameraInfo.getName();
                         notifyDataSetChanged();
                        break;
                }
            }
        };
        land_camera_list_rv.setAdapter(cameraInfoBaseRecyclerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isClick){
            EventBus.getDefault().post(new EventBusLandDetailsBean("YES",nid,cid,isOpen,isLive,name));
        }else {
            EventBus.getDefault().post(new EventBusLandDetailsBean("OK"));
        }
    }

    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandFragmentBean>(this,getLandRv(),R.layout.ly_activity_land_camera_list_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandFragmentBean landFragmentBean, int position) {

            }

            @Override
            public void clickEvent(int viewId, LandFragmentBean landFragmentBean, int position) {
                super.clickEvent(viewId, landFragmentBean, position);
                switch (viewId){

                }
            }
        };
        land_camera_list_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandFragmentBean> getLandRv(){
        mList=new ArrayList<>();
        LandFragmentBean landFragmentBean=new LandFragmentBean();
        mList.add(landFragmentBean);
        return mList;
    }
}
