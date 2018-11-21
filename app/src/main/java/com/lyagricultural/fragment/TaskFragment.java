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
 * 作者Administrator on 2018/8/27 0027 09:17
 */
public class TaskFragment extends Fragment{
    private static final String TAG = "TaskFragment";
    private View taskView;
    private RecyclerView task_rv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        taskView=inflater.inflate(R.layout.ly_fragment_task,null);
        initView();
        return taskView;
    }

    private void initView(){
        task_rv = taskView.findViewById(R.id.task_rv);

    }

}
