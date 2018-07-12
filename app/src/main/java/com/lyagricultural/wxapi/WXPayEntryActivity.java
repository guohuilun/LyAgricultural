package com.lyagricultural.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lyagricultural.utils.LyLog;
import com.lyagricultural.weixin.PayEntity;
import com.lyagricultural.weixin.PayUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, PayUtils.getWx_APP_ID());
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            PayEntity payEntity = new PayEntity();
            payEntity.setPayType(PayEntity.PayType.Wx_PAY);
            LyLog.d(TAG,"微信返回errCode  "+resp.errCode+resp.errStr);
            if (resp.errCode == 0) {
                LyLog.d(TAG,"微信返回数据  "+resp);
                payEntity.setStatus(PayEntity.Status.PAY_SUCCESS);
            } else {
                payEntity.setStatus(PayEntity.Status.PAY_FAIL);
            }
            EventBus.getDefault().post(payEntity);
        }
        finish();
    }
}