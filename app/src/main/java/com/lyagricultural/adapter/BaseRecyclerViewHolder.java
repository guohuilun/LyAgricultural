package com.lyagricultural.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyagricultural.R;
import com.lyagricultural.view.AmountView;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2017/6/8 0008.
 */
public class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder  {

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }
    public void setTxt(int viewId,String txt){
        TextView textView= (TextView) itemView.findViewById(viewId);
        textView.setText(txt);
    }

    public String getTxt(int viewId){
        TextView textView= (TextView) itemView.findViewById(viewId);
        return textView.getText().toString();
    }

    public void setTxtSize(int viewId,int textSize){
        TextView textView= (TextView) itemView.findViewById(viewId);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public void setCheckBoxTxt(int viewId,String txt){
        CheckBox checkBox= (CheckBox) itemView.findViewById(viewId);
        checkBox.setText(txt);
    }

    public void setBitMap(int viewId, Bitmap bitmap){
        ImageView imageView= (ImageView) itemView.findViewById(viewId);
        imageView.setImageBitmap(bitmap);
    }

    public void setTxtColor(int viewId, String color){
        TextView textView= (TextView) itemView.findViewById(viewId);
        textView.setTextColor(Color.parseColor(color));
    }

    public void setCheck(int viewId,Boolean bol){
        CheckBox checkBox= (CheckBox) itemView.findViewById(viewId);
        checkBox.setChecked(bol);
    }

    public Boolean isChecked(int viewId){
        CheckBox checkBox= (CheckBox) itemView.findViewById(viewId);
        return checkBox.isChecked();
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

    public void setWidth(int viewId,int w){
        LinearLayout linearLayout=itemView.findViewById(viewId);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);//定义一个LayoutParams
        layoutParams.setMargins(w,0,0,0);//4个参数按顺序分别是左上右下
        linearLayout.setLayoutParams(layoutParams);
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
            Glide.with(context).load(imgUrl).into(imageView);
        }
    }
/*
    public void setImgCrop(Context context, String imgUrl, int viewId){
        ImageView imageView= (ImageView) itemView.findViewById(viewId);
        if (!"".equals(imgUrl)){
            Picasso.with(context).load(imgUrl)
                    .resize(200,200)
                    .centerCrop()
                    .placeholder(R.drawable.empty)
                    .error(R.drawable.empty)
                    .into(imageView);
        }
    }*/

    //        设置控件是否可见
    public void setInVisibility(int viewId,int v){
        itemView.findViewById(viewId).setVisibility(v);
    }

    //        设置recyclerView里面的子recycleView
    public void setRecyclerViewItem(int viewId , RecyclerView.LayoutManager manager, BaseRecyclerAdapter baseRecyclerAdapter){
        RecyclerView recyclerView= (RecyclerView) itemView.findViewById(viewId);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(baseRecyclerAdapter);
    }


    public void setClick(final int viewId, final T t, final int position,final BaseRecyclerAdapter recyclerAdapter) {
        View view = itemView.findViewById(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerAdapter.clickEvent(viewId, t, position);
            }
        });
    }

    public void setBgResource(int viewId,int sourceId){
        View view=itemView.findViewById(viewId);
        view.setBackgroundResource(sourceId);
    }


   /* public void setRichText(int viewId,String txt){
        TextView textView= (TextView) itemView.findViewById(viewId);
        RichText.from(txt).into(textView);
    }*/

    public void setAmountViewTxt(int viewId,int v){
        AmountView mAmountView=itemView.findViewById(viewId);
        mAmountView.setAmount(v);
    }

    public void setAmountViewAllTxt(int viewId,int v){
        AmountView mAmountView=itemView.findViewById(viewId);
        mAmountView.setGoods_storage(v);
    }

    public void setAmountViewListener(int viewId,AmountView.OnAmountChangeListener listener){
        AmountView mAmountView=itemView.findViewById(viewId);
        mAmountView.setListener(listener);
    }

    public void setCheckBockListener(int viewId,CheckBox.OnCheckedChangeListener listener){
        CheckBox checkBox= (CheckBox) itemView.findViewById(viewId);
        checkBox.setOnCheckedChangeListener(listener);
    }



}
