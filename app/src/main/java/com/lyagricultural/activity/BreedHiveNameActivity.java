package com.lyagricultural.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.app.BaseActivity;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.utils.BannerUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zzhoujay.richtext.RichText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者Administrator on 2018/5/29 0029 13:39
 */
public class BreedHiveNameActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "BreedHiveNameActivity";
    private Banner breed_hive_name_banner;
    private LinearLayout breed_hive_name_content_ll;
    private Button breed_hive_name_button;
    private TextView breed_hive_name_rich_tv;
    private RichText richText;
    private LinearLayout breed_hive_name_claim_ll;
    private  List<Integer> ImageData = new ArrayList<>();
    private String title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_breed_hive_name);
        initView();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private void initView(){
        Intent intent=getIntent();
        if (intent!=null){
            title = intent.getStringExtra("text");
            setTitle(title);
        }
        breed_hive_name_banner=findViewById(R.id.breed_hive_name_banner);
        breed_hive_name_content_ll= findViewById(R.id.breed_hive_name_content_ll);
        breed_hive_name_button=findViewById(R.id.breed_hive_name_button);
        breed_hive_name_button.setOnClickListener(this);
        breed_hive_name_rich_tv=findViewById(R.id.breed_hive_name_rich_tv);
        breed_hive_name_claim_ll=findViewById(R.id.breed_hive_name_claim_ll);
        breed_hive_name_claim_ll.setOnClickListener(this);
        setImageData();
        setBanner();
    }

    private void setBanner(){
        breed_hive_name_banner.setDelayTime(3000)
                .setImages(ImageData)
                .setImageLoader(new BannerUtils.GlideImageLoader())
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        breed_hive_name_banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.breed_hive_name_button:
                startActivity(new Intent(this,BreedHiveNameLeasedActivity.class).putExtra("text",title));
                break;

            case R.id.breed_hive_name_claim_ll:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (richText!=null){
            richText.clear();
            richText = null;
        }
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sentParsms(EventBusDefaultBean bean) {//此方法类似于广播，任何地方都可以传递
        if ("OK".equals(bean.getMsg())){
            finish();
        }
    }
    /**
     * 测试数据
     */
    private List<Integer> setImageData(){
        ImageData.clear();
        ImageData.add(R.mipmap.ce_breed_bg);
        ImageData.add(R.mipmap.ce_breed_o_bg);
        return ImageData;
    }

}
