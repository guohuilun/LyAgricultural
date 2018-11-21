package com.lyagricultural.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lyagricultural.R;
import com.lyagricultural.adapter.ShopFragmentAdapter;
import com.lyagricultural.tablayout.TabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者Administrator on 2018/8/27 0027 09:17
 */
public class ManageFragment extends Fragment{
    private static final String TAG = "ManageFragment";
    private View manageView;
    private CommonTabLayout manage_ct;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] mIconUnselectIds = {
            R.mipmap.ce_account_defult_bg_t};
    private int[] mIconSelectIds = {
            R.mipmap.ce_account_defult_bg_t};
    private ViewPager manage_vp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        manageView=inflater.inflate(R.layout.ly_fragment_manage,null);
        initView();
        return manageView;
    }

    private void initView(){
        manage_ct=manageView.findViewById(R.id.manage_ct);
        manage_vp=manageView.findViewById(R.id.manage_vp);
        String[] mTitles = new String[]{"土地"};
        for (int i = 0; i <mTitles.length ; i++) {
            mTabEntities.add(new TabEntity(mTitles[i],mIconSelectIds[i],mIconUnselectIds[i]));
        }
        manage_vp.setAdapter(new ShopFragmentAdapter(getChildFragmentManager(),getFragments()));
        setViewPager();//关联tablayout和viewpager
    }

    private List<Fragment> getFragments() {
        List<Fragment> mList = new ArrayList<>();
        mList.add(new ManageLandFragment());
        return mList;
    }

    private void setViewPager(){
        manage_ct.setTabData(mTabEntities);
        manage_ct.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                manage_vp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        manage_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                manage_ct.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        manage_vp.setCurrentItem(0);
    }

}
