package com.lyagricultural.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;

/**
 * 作者Administrator on 2018/7/27 0027 11:27
 */
public class BreedHiveActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "BreedHiveActivity";

    private Button breed_hive_button;
    private RecyclerView breed_hive_rv;
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.breed_hive_button:
                break;
        }
    }
}
