package com.lyagricultural.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lyagricultural.R;
import com.lyagricultural.activity.AccountAboutUsActivity;
import com.lyagricultural.activity.AccountAddressActivity;
import com.lyagricultural.activity.AccountMonetaryDetailsActivity;
import com.lyagricultural.activity.AccountOrderActivity;
import com.lyagricultural.activity.AccountPersonalActivity;
import com.lyagricultural.activity.AccountRechargeActivity;
import com.lyagricultural.activity.AccountSeedOutActivity;
import com.lyagricultural.bean.DefaultBean;
import com.lyagricultural.bean.ShopSeedBean;
import com.lyagricultural.constant.AppConstant;
import com.lyagricultural.gen.ShopSeedDao;
import com.lyagricultural.http.LecoOkHttpUtil;
import com.lyagricultural.utils.CheckNetworkUtils;
import com.lyagricultural.utils.LyLog;
import com.lyagricultural.utils.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * 作者Administrator on 2018/5/22 0022 12:27
 */
public class AccountFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AccountFragment";
    private View accountView;
    private ImageView ly_fragment_account_iv;
    private TextView ly_fragment_account_money_tv;
    private TextView ly_fragment_account_recharge_tv;
    private TextView ly_fragment_account_monetary_details_tv;
    private RelativeLayout ly_fragment_account_order_rl;
    private RelativeLayout ly_fragment_account_distribution_rl;
    private RelativeLayout ly_fragment_account_address_rl;
    private RelativeLayout ly_fragment_account_information_rl;
    private RelativeLayout ly_fragment_account_my_rl;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        accountView=inflater.inflate(R.layout.ly_fragment_account,null);
        initView();
        return accountView;
    }

    private void initView(){
        ly_fragment_account_iv=accountView.findViewById(R.id.ly_fragment_account_iv);
        ly_fragment_account_money_tv=accountView.findViewById(R.id.ly_fragment_account_money_tv);
        ly_fragment_account_recharge_tv=accountView.findViewById(R.id.ly_fragment_account_recharge_tv);
        ly_fragment_account_monetary_details_tv=accountView.findViewById(R.id.ly_fragment_account_monetary_details_tv);
        ly_fragment_account_order_rl=accountView.findViewById(R.id.ly_fragment_account_order_rl);
        ly_fragment_account_distribution_rl=accountView.findViewById(R.id.ly_fragment_account_distribution_rl);
        ly_fragment_account_address_rl=accountView.findViewById(R.id.ly_fragment_account_address_rl);
        ly_fragment_account_information_rl=accountView.findViewById(R.id.ly_fragment_account_information_rl);
        ly_fragment_account_my_rl=accountView.findViewById(R.id.ly_fragment_account_my_rl);

        ly_fragment_account_recharge_tv.setOnClickListener(this);
        ly_fragment_account_monetary_details_tv.setOnClickListener(this);
        ly_fragment_account_order_rl.setOnClickListener(this);
        ly_fragment_account_distribution_rl.setOnClickListener(this);
        ly_fragment_account_address_rl.setOnClickListener(this);
        ly_fragment_account_information_rl.setOnClickListener(this);
        ly_fragment_account_my_rl.setOnClickListener(this);
        initAccountData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_fragment_account_recharge_tv:
                startActivity(new Intent(getActivity(), AccountRechargeActivity.class));
                break;
            case R.id.ly_fragment_account_monetary_details_tv:
                startActivity(new Intent(getActivity(), AccountMonetaryDetailsActivity.class));
                break;
            case R.id.ly_fragment_account_order_rl:
                startActivity(new Intent(getActivity(), AccountOrderActivity.class));
                break;
            case R.id.ly_fragment_account_distribution_rl:
                startActivity(new Intent(getActivity(), AccountSeedOutActivity.class));
                break;
            case R.id.ly_fragment_account_address_rl:
                startActivity(new Intent(getActivity(), AccountAddressActivity.class));
                break;
            case R.id.ly_fragment_account_information_rl:
                startActivity(new Intent(getActivity(), AccountAboutUsActivity.class));
                break;
            case R.id.ly_fragment_account_my_rl:
                startActivity(new Intent(getActivity(), AccountPersonalActivity.class));
                break;
        }
    }


    /**
     *  获取账户余额   -网络请求
     */
    private void initAccountData(){
        if (CheckNetworkUtils.checkNetworkAvailable(getActivity())){
            LecoOkHttpUtil lecoOkHttpUtil=new LecoOkHttpUtil();
            lecoOkHttpUtil.post().url(AppConstant.APP_USER_ACCOUNT)
                    .addParams("userId", SpUtils.getSp("userid",getActivity(),"LoginActivity"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LyLog.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            LyLog.i(TAG,"获取账户余额 = " +response);
                            Gson gson=new Gson();
                            DefaultBean parse=gson.fromJson(response,DefaultBean.class);
                            if ("OK".equals(parse.getStatus())){
                                ly_fragment_account_money_tv.setText(parse.getData());
                            }
                        }
                    });
        }
    }
}
