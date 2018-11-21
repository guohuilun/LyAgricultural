package com.lyagricultural.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;

/**
 * 作者Administrator on 2018/6/4 0004 17:42
 */
public class ManageDetailsActivity extends BaseActivity {
    private RecyclerView manage_details_rv;
    private RelativeLayout manage_details_tend_rl;
    private TextView manage_details_tend_tv;
    private RelativeLayout manage_details_state_rl;
    private TextView manage_details_state_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_manage_details);
        setTitle("");
        initView();
    }

    private void initView(){

    }
}
