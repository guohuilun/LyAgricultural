package com.lyagricultural.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyagricultural.R;
import com.lyagricultural.activity.LandDetailsNameActivity;
import com.lyagricultural.adapter.BaseRecyclerAdapter;
import com.lyagricultural.adapter.BaseRecyclerViewHolder;
import com.lyagricultural.bean.EventBusDefaultBean;
import com.lyagricultural.bean.EventBusLandDetailsBean;
import com.lyagricultural.cebean.LandFragmentBean;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.LyToast;
import com.lyagricultural.view.AmountView;
import com.lyagricultural.view.TextSpan;
import com.yykj.mob.share.share.SharePlatformUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 作者Administrator on 2018/6/5 0005 17:27
 */
public class LandDialogFragment extends DialogFragment implements View.OnClickListener,PlatformActionListener {
    private static final String TAG = "LandDialogFragment";
    private View landDialogView;

    private String vegetable;
    private int vegetableIv;
//     操作界面
    private LinearLayout dialog_fragment_hand_content;
    private LinearLayout dialog_fragment_hand_cancel_ll;
    private ImageView dialog_fragment_hand_iv;
    private TextView dialog_fragment_hand_tv;
    private RecyclerView dialog_fragment_hand_rv;
    private TextView dialog_fragment_hand_money_tv;
    private TextView dialog_fragment_hand_money_o_tv;
    private LinearLayout dialog_fragment_hand_balance_ll;
    private TextView dialog_fragment_hand_buy_tv;
    private Button dialog_fragment_hand_button;
    private AmountView dialog_fragment_land_av;
//    成功界面
    private LinearLayout dialog_fragment_hand_content_success;
    private LinearLayout dialog_fragment_hand_qq_ll;
    private LinearLayout dialog_fragment_hand_wei_xin_ll;
    private LinearLayout dialog_fragment_hand_wei_xin_circle_ll;


    private  BaseRecyclerAdapter<LandFragmentBean> baseRecyclerAdapter;
    private  List<LandFragmentBean> mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        landDialogView=inflater.inflate(R.layout.ly_dialog_fragment_land,null);
        initView();
        return landDialogView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }

    private void initView(){
        Bundle bundle = getArguments();
        if (bundle!=null){
            vegetable = bundle.getString("vegetable");
            vegetableIv = bundle.getInt("vegetableIv");
        }

//     操作界面（开始）
        dialog_fragment_hand_content=landDialogView.findViewById(R.id.dialog_fragment_hand_content);
        dialog_fragment_hand_cancel_ll=landDialogView.findViewById(R.id.dialog_fragment_hand_cancel_ll);
        dialog_fragment_hand_iv=landDialogView.findViewById(R.id.dialog_fragment_hand_iv);
        dialog_fragment_hand_tv=landDialogView.findViewById(R.id.dialog_fragment_hand_tv);
        dialog_fragment_hand_rv=landDialogView.findViewById(R.id.dialog_fragment_hand_rv);
        dialog_fragment_hand_money_tv=landDialogView.findViewById(R.id.dialog_fragment_hand_money_tv);
        dialog_fragment_hand_money_o_tv=landDialogView.findViewById(R.id.dialog_fragment_hand_money_o_tv);
        dialog_fragment_hand_balance_ll=landDialogView.findViewById(R.id.dialog_fragment_hand_balance_ll);
        dialog_fragment_hand_buy_tv=landDialogView.findViewById(R.id.dialog_fragment_hand_buy_tv);
        dialog_fragment_hand_button=landDialogView.findViewById(R.id.dialog_fragment_hand_button);
        dialog_fragment_land_av=landDialogView.findViewById(R.id.dialog_fragment_land_av);
        SpannableStringBuilder spannableMoney = new SpannableStringBuilder("货币不足，请充值");
        spannableMoney.setSpan(new TextSpan(),6,8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        dialog_fragment_hand_buy_tv.setText(spannableMoney);
        dialog_fragment_hand_cancel_ll.setOnClickListener(this);
        dialog_fragment_hand_button.setOnClickListener(this);
        dialog_fragment_hand_tv.setText(vegetable);
        dialog_fragment_hand_iv.setImageResource(vegetableIv);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),5);
        dialog_fragment_hand_rv.setLayoutManager(layoutManager);
        dialog_fragment_land_av.setAmount(1);
//        setLandRv();
//     操作界面（结束）

//     成功界面（开始）
        dialog_fragment_hand_content_success=landDialogView.findViewById(R.id.dialog_fragment_hand_content_success);
        dialog_fragment_hand_qq_ll=landDialogView.findViewById(R.id.dialog_fragment_hand_qq_ll);
        dialog_fragment_hand_wei_xin_ll=landDialogView.findViewById(R.id.dialog_fragment_hand_wei_xin_ll);
        dialog_fragment_hand_wei_xin_circle_ll=landDialogView.findViewById(R.id.dialog_fragment_hand_wei_xin_circle_ll);
        dialog_fragment_hand_qq_ll.setOnClickListener(this);
        dialog_fragment_hand_wei_xin_ll.setOnClickListener(this);
        dialog_fragment_hand_wei_xin_circle_ll.setOnClickListener(this);
//     成功界面（结束）
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        LyLog.i(TAG,"执行没得 = onDismiss");
        super.onDismiss(dialog);
    }



    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.dialog_fragment_hand_cancel_ll:
               getDialog().dismiss();
               break;
           case R.id.dialog_fragment_hand_button:
               dialog_fragment_hand_content.setVisibility(View.GONE);
               dialog_fragment_hand_content_success.setVisibility(View.VISIBLE);
               break;
           case R.id.dialog_fragment_hand_qq_ll:
               SharePlatformUtils.showShare(getActivity(), QQ.NAME, "利用了几", "http://www.baidu.com", "http://img8.zol.com.cn/bbs/upload/23597/23596811.jpg", "",this);
               break;
           case R.id.dialog_fragment_hand_wei_xin_ll:
               SharePlatformUtils.showShare(getActivity(), Wechat.NAME,"利用了几", "http://www.baidu.com", "http://img8.zol.com.cn/bbs/upload/23597/23596811.jpg", "",this);
               break;
           case R.id.dialog_fragment_hand_wei_xin_circle_ll:
               SharePlatformUtils.showShare(getActivity(), WechatMoments.NAME,"利用了几", "http://www.baidu.com", "http://img8.zol.com.cn/bbs/upload/23597/23596811.jpg", "",this);
               break;
       }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        LyLog.i(TAG, "onComplete: "+platform+"==="+i );
//        getDialog().dismiss();
        EventBus.getDefault().post(new EventBusLandDetailsBean("OK"));
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        LyLog.i(TAG, "onError: "+platform+"==="+i );
    }

    @Override
    public void onCancel(Platform platform, int i) {
        LyLog.i(TAG, "onCancel: "+platform+"==="+i );
//        getDialog().dismiss();
        EventBus.getDefault().post(new EventBusLandDetailsBean("OK"));
    }

    private void setLandRv(){
        baseRecyclerAdapter=new BaseRecyclerAdapter<LandFragmentBean>(getActivity(),getLandRv(),R.layout.ly_dialog_fragment_land_rv_item) {
            @Override
            public void bindData(BaseRecyclerViewHolder holder, LandFragmentBean landFragmentBean, int position) {
            }
        };
        dialog_fragment_hand_rv.setAdapter(baseRecyclerAdapter);
    }

    private List<LandFragmentBean> getLandRv(){
        mList=new ArrayList<>();
        LandFragmentBean landFragmentBean=new LandFragmentBean();
        mList.add(landFragmentBean);
        return mList;
    }
}
