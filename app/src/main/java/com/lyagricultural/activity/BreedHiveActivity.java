package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.lyagricultural.R;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.cebean.LandDetailsNameBean;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.utils.ProgressDialogUtils;
import com.tongguan.yuanjian.family.Utils.PersonManager;
import com.tongguan.yuanjian.family.Utils.RequestCallback;
import com.tongguan.yuanjian.family.Utils.req.LoginRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者Administrator on 2018/7/27 0027 11:27
 */
public class BreedHiveActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "BreedHiveActivity";

    private Button breed_hive_button;
    private RecyclerView breed_hive_rv;
    private BaseRecyclerAdapter<LandDetailsNameBean> baseRecyclerAdapter;
    private List<LandDetailsNameBean> mList;

    private LoginRequest _loginReq =new LoginRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_breed_hive);
        setTitle("蜂箱");
        initView();
    }

    private void initView(){
        breed_hive_button=findViewById(R.id.breed_hive_button);
        breed_hive_rv=findViewById(R.id.breed_hive_rv);
        breed_hive_button.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        breed_hive_rv.setLayoutManager(layoutManager);
        setLandRv();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.breed_hive_button:
                        setYJLogin("ZMSFC001");
//                startActivity(new Intent(BreedHiveActivity.this,BreedHiveNameMyActivity.class));
                break;
        }
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
            ProgressDialogUtils.showProgressDialog(this,"正在加载摄像头，请稍后...");
            _loginReq.setRequestCallback(new RequestCallback(){
                @Override
                public void onPostExecute(int result) {
                    LyLog.i(TAG,"在线视频登录结果 = "+result);
                    if (result!=0){
                        ProgressDialogUtils.closeProgressDialog();
                        startActivity(new Intent(BreedHiveActivity.this,BreedHiveNameMyActivity.class));
                    }else {
                        ProgressDialogUtils.closeProgressDialog();
                        LyToast.shortToast(BreedHiveActivity.this,"获取摄像头失败");
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
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandDetailsNameBean>(this,getLandRv(),R.layout.ly_activity_breed_hive_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandDetailsNameBean landFragmentBean, int position) {
                holder.setBgResource(R.id.breedHiveIv,landFragmentBean.getLand_details_name_rv_iv());
                holder.setTxt(R.id.breedHiveNameTv,landFragmentBean.getLand_details_name_rv_tv());
                holder.setTxt(R.id.breedHiveAddressTv,landFragmentBean.getLand_details_name_rv_tv_o());
                holder.setTxt(R.id.breedHiveAddressOTv,landFragmentBean.getLand_details_name_rv_tv_t());
                holder.setClick(R.id.breedHiveLl,landFragmentBean,position,baseRecyclerAdapter);
            }

            @Override
            public void clickEvent(int viewId, LandDetailsNameBean landFragmentBean, int position) {
                super.clickEvent(viewId, landFragmentBean, position);
                switch (viewId){
                    case R.id.breedHiveLl:
                        startActivity(new Intent(BreedHiveActivity.this,BreedHiveNameActivity.class)
                                .putExtra("text",landFragmentBean.getLand_details_name_rv_tv()));
                        break;
                }
            }
        };
        breed_hive_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandDetailsNameBean> getLandRv(){
        mList=new ArrayList<>();
        LandDetailsNameBean landFragmentBean=new LandDetailsNameBean
                (R.mipmap.ce_beehive_picture,"蜂箱1"
                        ,"渝北区","照母山公园");
        mList.add(landFragmentBean);
        landFragmentBean=new LandDetailsNameBean
                (R.mipmap.ce_beehive_picture,"蜂箱2"
                        ,"渝北区","照母山公园");
        mList.add(landFragmentBean);
        landFragmentBean=new LandDetailsNameBean
                (R.mipmap.ce_beehive_picture,"蜂箱3"
                        ,"渝北区","照母山公园");
        mList.add(landFragmentBean);
        return mList;
    }
}
