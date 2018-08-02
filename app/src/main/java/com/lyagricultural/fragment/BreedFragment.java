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
 * 作者Administrator on 2018/7/10 0010 14:46
 */
public class BreedFragment  extends Fragment {
    private View breedView;
    private RecyclerView breed_rv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        breedView=inflater.inflate(R.layout.ly_fragment_breed,null);
        initView();
        return breedView;
    }
    private void initView(){
        breed_rv=breedView.findViewById(R.id.breed_rv);
    }
}
