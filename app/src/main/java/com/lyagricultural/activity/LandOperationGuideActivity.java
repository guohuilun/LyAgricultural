package com.lyagricultural.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.DefaultBean;
import com.lyagricultural.bean.EventBusLandDetailsBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zzhoujay.richtext.RichText;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/6/5 0005 16:22
 */
public class LandOperationGuideActivity extends BaseActivity {
    private static final String TAG = "LandOperationGuideActivity";
    private TextView land_operation_guide_rich_tv;
    private RichText richText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_land_operation_guide);
        setTitle("操作指南");
        initView();
    }

    private void initView(){
        land_operation_guide_rich_tv=findViewById(R.id.land_operation_guide_rich_tv);
        initHelpSelect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (richText!=null){
            richText.clear();
            richText = null;
        }
        EventBus.getDefault().post(new EventBusLandDetailsBean("OK"));
    }


    /**
     *  获取帮助   -网络请求
     */
    private void initHelpSelect(){
        if (CheckNetworkUtils.checkNetworkAvailable(this)){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_HELP_SELECT)
                    .addParams("helpType", "Land_Help")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取帮助 = " +response);
                            Gson gson=new Gson();
                            DefaultBean parse=gson.fromJson(response,DefaultBean.class);
                            if ("OK".equals(parse.getStatus())){
                                richText = RichText.from(parse.getData())
                                        .into(land_operation_guide_rich_tv);
                            }
                        }
                    });
        }
    }

}
