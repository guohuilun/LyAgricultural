package com.lyagricultural.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.adapter.ShopFragmentAdapter;
import com.lyagricultural.bean.EventBusDefaultBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者Administrator on 2018/5/22 0022 12:27
 */
public class ShopFragment extends Fragment implements View.OnClickListener{
    private View shopView;
    private ViewPager shop_vp;
    private RelativeLayout shop_land_rl;
    private RelativeLayout shop_seed_rl;
    private RelativeLayout shop_props_rl;
    private ImageView shop_land_iv;
    private ImageView shop_seed_iv;
    private ImageView shop_props_iv;
    private TextView shop_land_tv;
    private TextView shop_seed_tv;
    private TextView shop_props_tv;
    private View view_line;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        shopView=inflater.inflate(R.layout.ly_fragment_shop,null);
        initView();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        return shopView;
    }

    private void initView(){
        shop_vp=shopView.findViewById(R.id.shop_vp);
//        rl
        shop_land_rl=shopView.findViewById(R.id.shop_land_rl);
        shop_seed_rl=shopView.findViewById(R.id.shop_seed_rl);
        shop_props_rl=shopView.findViewById(R.id.shop_props_rl);
//        image
        shop_land_iv=shopView.findViewById(R.id.shop_land_iv);
        shop_seed_iv=shopView.findViewById(R.id.shop_seed_iv);
        shop_props_iv=shopView.findViewById(R.id.shop_props_iv);
//        text
        shop_land_tv=shopView.findViewById(R.id.shop_land_tv);
        shop_seed_tv=shopView.findViewById(R.id.shop_seed_tv);
        shop_props_tv=shopView.findViewById(R.id.shop_props_tv);

        view_line=shopView.findViewById(R.id.view_line);


        shop_land_tv.setTextColor(Color.parseColor("#77C34F"));
        shop_land_iv.setImageResource(R.mipmap.ly_fragment_shop_land_down);
        shop_land_rl.setBackgroundColor(Color.parseColor("#FFFFFF"));

        shop_land_rl.setOnClickListener(this);
        shop_seed_rl.setOnClickListener(this);
        shop_props_rl.setOnClickListener(this);
        setViewPager();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shop_land_rl:
                setColor(0);
                setImage(0);
                shop_vp.setCurrentItem(0);
                break;
            case R.id.shop_seed_rl:
                setColor(1);
                setImage(1);
                shop_vp.setCurrentItem(1);
                break;
            case R.id.shop_props_rl:
                setColor(2);
                setImage(2);
                shop_vp.setCurrentItem(2);
                break;
        }
    }

    private void setViewPager(){
        shop_vp.setAdapter(new ShopFragmentAdapter(getChildFragmentManager(),getFragments()));
        shop_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    setColor(0);
                    setImage(0);
                }else if (position==1){
                    setColor(1);
                    setImage(1);
                }else if (position==2){
                    setColor(2);
                    setImage(2);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private List<Fragment> getFragments() {
        List<Fragment> mList = new ArrayList<>();
        mList.add(new ShopLandFragment());
        mList.add(new ShopSeedFragment());
        mList.add(new ShopPropsFragment());
        return mList;
    }

    private void setColor(int position) {
        shop_land_tv.setTextColor(Color.parseColor("#FFFFFF"));
        shop_seed_tv.setTextColor(Color.parseColor("#FFFFFF"));
        shop_props_tv.setTextColor(Color.parseColor("#FFFFFF"));
        shop_land_rl.setBackgroundColor(Color.parseColor("#77C34F"));
        shop_seed_rl.setBackgroundColor(Color.parseColor("#77C34F"));
        shop_props_rl.setBackgroundColor(Color.parseColor("#77C34F"));
        switch (position) {
            case 0:
                shop_land_tv.setTextColor(Color.parseColor("#77C34F"));
                shop_land_rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 1:
                shop_seed_tv.setTextColor(Color.parseColor("#77C34F"));
                shop_seed_rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 2:
                shop_props_tv.setTextColor(Color.parseColor("#77C34F"));
                shop_props_rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }

    private void setImage(int position){
        shop_land_iv.setImageResource(R.mipmap.ly_fragment_shop_land_up);
        shop_seed_iv.setImageResource(R.mipmap.ly_fragment_shop_seed_up);
        shop_props_iv.setImageResource(R.mipmap.ly_fragment_shop_props_up);
        switch (position) {
            case 0:
                shop_land_iv.setImageResource(R.mipmap.ly_fragment_shop_land_down);
                break;
            case 1:
                shop_seed_iv.setImageResource(R.mipmap.ly_fragment_shop_seed_down);
                break;
            case 2:
                shop_props_iv.setImageResource(R.mipmap.ly_fragment_shop_props_down);
                break;
        }
    }

    /**
     * 封装一个方法来从Activity中获取资源DPI的尺寸信息
     * 将Dp像素转换为pX
     *
     * @param dpNumber
     * @return
     */
    private int dp2px(int dpNumber) {
        return (int) (getActivity().getResources().getDisplayMetrics().density * dpNumber + 0.5f);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sentParsms(EventBusDefaultBean bean) {//此方法类似于广播，任何地方都可以传递
        if ("ShopFragmentSwitch".equals(bean.getMsg())){
            setColor(1);
            setImage(1);
            shop_vp.setCurrentItem(1);
        }else if ("ShopFragmentSwitchLand".equals(bean.getMsg())){
            setColor(0);
            setImage(0);
            shop_vp.setCurrentItem(0);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }


}
