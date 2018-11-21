package com.lyagricultural.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;

/**
 * 作者Administrator on 2018/8/29 0029 09:24
 */
public class TaskRecordActivity extends BaseActivity {
    private RecyclerView task_record_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_task_record);
        initView();
    }

    private void initView(){
        task_record_rv = findViewById(R.id.task_record_rv);
    }

}
