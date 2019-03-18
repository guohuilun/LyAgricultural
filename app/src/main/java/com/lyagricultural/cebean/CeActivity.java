package com.lyagricultural.cebean;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.lyagricultural.R;
import com.lyagricultural.utils.GradientDrawableUtils;

public class CeActivity extends Activity {
    private LinearLayout cornersLinearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_breed_hive_name_my_rv_t_item);
        cornersLinearLayout=findViewById(R.id.cornersLinearLayout);
//        cornersLinearLayout.setCornerRadius(50);
//        cornersLinearLayout.setBackgroundResource(R.color.layout_base_title_bg);
        cornersLinearLayout.setBackground(GradientDrawableUtils.setGDrawable(16,"#DD2323"));
    }
}
