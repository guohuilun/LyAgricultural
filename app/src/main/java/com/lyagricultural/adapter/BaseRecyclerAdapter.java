package com.lyagricultural.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder>{
    private Context context;
    private List<T> ts;
    private int layoutId;
    private View view;

    public BaseRecyclerAdapter(Context context, List<T> ts, int layoutId) {
        this.context = context;
        this.ts = ts;
        this.layoutId = layoutId;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(layoutId,parent,false);
        BaseRecyclerViewHolder holder=new BaseRecyclerViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        T t=ts.get(position);
        bindData(holder,t,position);

    }

    @Override
    public int getItemCount() {
        return ts.size();
    }

     public abstract void bindData(BaseRecyclerViewHolder holder,T t,int position);

     public void clickEvent(int viewId,T t,int position){

    }


    /*   public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
        public BaseRecyclerViewHolder(View itemView) {
            super(itemView);
        }
        public void setTxt(int viewId,String txt){
            TextView textView= (TextView) itemView.findViewById(viewId);
            textView.setText(txt);
        }

        public void setLinHeight(int viewId,int h){
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) itemView.findViewById(viewId).getLayoutParams();
            params.height=h;
            itemView.findViewById(viewId).setLayoutParams(params);
        }

        public void setRelHeight(int viewId,int h){
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) itemView.findViewById(viewId).getLayoutParams();
            params.height=h;
            itemView.findViewById(viewId).setLayoutParams(params);
        }
        public void setRelWidth(int viewId,int w){
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) itemView.findViewById(viewId).getLayoutParams();
            params.width=w;
            itemView.findViewById(viewId).setLayoutParams(params);
        }

        public void setLinWidth(int viewId,int w){
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) itemView.findViewById(viewId).getLayoutParams();
            params.width=w;
            itemView.findViewById(viewId).setLayoutParams(params);
        }

        public void setImg(Context context, String imgUrl, int viewId){
            ImageView imageView= (ImageView) itemView.findViewById(viewId);
            if (!"".equals(imgUrl)){
                Picasso.with(context).load(imgUrl).into(imageView);
            }
        }
//        设置控件是否可见
        public void setInVisibility(int viewId,int v){
            itemView.findViewById(viewId).setVisibility(v);
        }

//        设置recyclerView里面的子recycleView
        public void setRecyclerViewItem(int viewId , RecyclerView.LayoutManager manager,BaseRecyclerAdapter baseRecyclerAdapter){
            RecyclerView  recyclerView= (RecyclerView) itemView.findViewById(viewId);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(baseRecyclerAdapter);
        }


        public void setClick(final int viewId, final T t, final int position) {
            View view = itemView.findViewById(viewId);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickEvent(viewId, t, position);
                }
            });
        }
    }*/

}
