package com.lyagricultural.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.EventBusLandDetailsBean;
import com.zzhoujay.richtext.RichText;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者Administrator on 2018/6/5 0005 16:22
 */
public class LandOperationGuideActivity extends BaseActivity {
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
}
