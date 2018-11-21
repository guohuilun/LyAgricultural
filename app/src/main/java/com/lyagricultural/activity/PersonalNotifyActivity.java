package com.lyagricultural.activity;

import android.os.Bundle;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.SpSimpleUtils;
import com.lyagricultural.view.SwitchButton;


/**
 * 作者Administrator on 2018/8/29 0029 10:22
 */
public class PersonalNotifyActivity extends BaseActivity implements SwitchButton.OnCheckedChangeListener{
    private static final String TAG = "PersonalNotifyActivity";
    private SwitchButton pn_status_sb;
    private SwitchButton pn_sound_sb;
    private SwitchButton pn_shock_sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_personal_notify);
        setTitle("通知管理");
        initView();
    }

    private void initView(){
        pn_status_sb=findViewById(R.id.pn_status_sb);
        pn_sound_sb=findViewById(R.id.pn_sound_sb);
        pn_shock_sb=findViewById(R.id.pn_shock_sb);
        pn_status_sb.setOnCheckedChangeListener(this);
        pn_sound_sb.setOnCheckedChangeListener(this);
        pn_shock_sb.setOnCheckedChangeListener(this);
        setSp();
    }

    private void setSp(){
      /*  if (!"".equals(SpSimpleUtils.getSp("isCheckedStatus", this, "PersonalNotifyActivity"))){
            if ("true".equals(SpSimpleUtils.getSp("isCheckedStatus", this, "PersonalNotifyActivity"))){
                pn_status_sb.setOnCheck(true);
            }
        }else {
             LyLog.i(TAG,"isCheckedStatus是Null");
              pn_status_sb.setOnCheck(true);
        }*/

        if (!"".equals(SpSimpleUtils.getSp("isCheckedSound", this, "PersonalNotifyActivity"))){
            if ("true".equals(SpSimpleUtils.getSp("isCheckedSound", this, "PersonalNotifyActivity"))){
                pn_sound_sb.setOnCheck(true);
            }
        }else {
            LyLog.i(TAG,"isCheckedSound是Null");
            pn_sound_sb.setOnCheck(true);
        }


        if (!"".equals(SpSimpleUtils.getSp("isCheckedShock", this, "PersonalNotifyActivity"))){
            if ("true".equals(SpSimpleUtils.getSp("isCheckedShock", this, "PersonalNotifyActivity"))){
                pn_shock_sb.setOnCheck(true);
            }
        }else {
            LyLog.i(TAG,"isCheckedShock是Null");
            pn_shock_sb.setOnCheck(false);
        }



    }


    @Override
    public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.pn_status_sb:
//                LyLog.i(TAG,"状态栏按钮变化 status = "+isChecked);
//                SpSimpleUtils.removeSharedPreference("isCheckedStatus",this,"PersonalNotifyActivity");
//                SpSimpleUtils.saveSp("isCheckedStatus",""+isChecked,this,"PersonalNotifyActivity");
                break;
            case R.id.pn_sound_sb:
                LyLog.i(TAG,"声音按钮变化 sound = "+isChecked);
                SpSimpleUtils.removeSharedPreference("isCheckedSound",this,"PersonalNotifyActivity");
                SpSimpleUtils.saveSp("isCheckedSound",""+isChecked,this,"PersonalNotifyActivity");
                if (isChecked){
                    pn_shock_sb.setOnCheck(false);
                    SpSimpleUtils.removeSharedPreference("isCheckedShock",this,"PersonalNotifyActivity");
                    SpSimpleUtils.saveSp("isCheckedShock","false",this,"PersonalNotifyActivity");
                }else {
                    pn_shock_sb.setOnCheck(true);
                    SpSimpleUtils.removeSharedPreference("isCheckedShock",this,"PersonalNotifyActivity");
                    SpSimpleUtils.saveSp("isCheckedShock","true",this,"PersonalNotifyActivity");
                }
                break;
            case R.id.pn_shock_sb:
                LyLog.i(TAG,"震动按钮变化 shock = "+isChecked);
                SpSimpleUtils.removeSharedPreference("isCheckedShock",this,"PersonalNotifyActivity");
                SpSimpleUtils.saveSp("isCheckedShock",""+isChecked,this,"PersonalNotifyActivity");
                if (isChecked){
                    pn_sound_sb.setOnCheck(false);
                    SpSimpleUtils.removeSharedPreference("isCheckedSound",this,"PersonalNotifyActivity");
                    SpSimpleUtils.saveSp("isCheckedSound","false",this,"PersonalNotifyActivity");
                }else {
                    pn_sound_sb.setOnCheck(true);
                    SpSimpleUtils.removeSharedPreference("isCheckedSound",this,"PersonalNotifyActivity");
                    SpSimpleUtils.saveSp("isCheckedSound","true",this,"PersonalNotifyActivity");
                }
                break;
        }
    }

}
