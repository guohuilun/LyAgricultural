package com.lyagricultural.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyagricultural.R;
import com.lyagricultural.activity.BreedHiveActivity;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.cebean.LandFragmentBean;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者Administrator on 2018/7/10 0010 14:46
 */
public class BreedFragment  extends Fragment {
    private static final String TAG = "BreedFragment";
    private View breedView;
    private RecyclerView breed_rv;
    private BaseRecyclerAdapter<LandFragmentBean> baseRecyclerAdapter;
    private List<LandFragmentBean> mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        breedView=inflater.inflate(R.layout.ly_fragment_breed,null);
        initView();
        return breedView;
    }
    private void initView(){
        breed_rv = breedView.findViewById(R.id.breed_rv);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),4);
        breed_rv.setLayoutManager(layoutManager);
        setLandRv();
    }

    /**
     * 测试数据
     */
    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandFragmentBean>(getActivity(),getLandRv(),R.layout.ly_fragment_breed_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandFragmentBean landFragmentBean, int position) {
                holder.setClick(R.id.breed_rv_ll,landFragmentBean,position,baseRecyclerAdapter);
            }

            @Override
            public void clickEvent(int viewId, LandFragmentBean landFragmentBean, int position) {
                super.clickEvent(viewId, landFragmentBean, position);
                switch (viewId){
                    case R.id.breed_rv_ll:
                        startActivity(new Intent(getActivity(),BreedHiveActivity.class));
                        break;
                }
            }
        };
        breed_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandFragmentBean> getLandRv(){
        mList=new ArrayList<>();
        LandFragmentBean landFragmentBean=new LandFragmentBean();
        mList.add(landFragmentBean);
        return mList;
    }
}
