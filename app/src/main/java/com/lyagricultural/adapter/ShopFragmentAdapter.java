package com.lyagricultural.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者Administrator on 2018/5/29 0029 10:46
 */
public class ShopFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public ShopFragmentAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList==null?null:mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList==null?0:mFragmentList.size();
    }

    /**
     *避免fragment重复加载网络数据，删掉super（XXX）
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
