package com.lyagricultural.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class ViewPagerImageAdapter extends PagerAdapter {
    private List<ImageView> imageViewList;

    public ViewPagerImageAdapter(List<ImageView> imageViewList) {
        this.imageViewList = imageViewList;
    }




    @Override
    public int getCount() {
        return imageViewList==null?0:imageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

//    创建item
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViewList.get(position));
        return imageViewList.get(position);
    }
//    删除item
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewList.get(position));
    }




}
