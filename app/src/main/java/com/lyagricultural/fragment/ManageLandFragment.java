package com.lyagricultural.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyagricultural.R;

/**
 * 作者Administrator on 2018/8/27 0027 13:51
 */
public class ManageLandFragment extends Fragment{
    private View manageLandView;
    private RecyclerView manage_land_rv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        manageLandView=inflater.inflate(R.layout.ly_fragment_manage_land,null);
        initView();
        return manageLandView;
    }

    private void initView(){
        manage_land_rv=manageLandView.findViewById(R.id.manage_land_rv);
    }
}
