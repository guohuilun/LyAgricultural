package com.lyagricultural.weixin;

import android.app.Activity;
import android.widget.Toast;


import com.lyagricultural.R;
import com.lyagricultural.bean.AccountRechargePayBean;
import com.lyagricultural.utils.LyLog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 支付工具类
 * Created by cheng on 2016/8/13.
 */
public class PayUtils {
    private static final String TAG = "PayUtils";
    PayEntity payEntity = new PayEntity();
    Activity context;
    static IWXAPI iwxapi;
    static String Wx_APP_ID;

    public PayUtils(Activity context) {
        this.context = context;
    }

    public static String getWx_APP_ID() {
        return Wx_APP_ID;
    }

    /**
     * 微信支付
     */
    public void wxPay(AccountRechargePayBean.LyDataBean lyDataBean) {
        payEntity.setPayType(PayEntity.PayType.Wx_PAY);
        if (lyDataBean == null) {
            Toast.makeText(context, R.string.failure_pay_weixin, Toast.LENGTH_SHORT);
            return;
        }
        if (iwxapi == null) {
            iwxapi = WXAPIFactory.createWXAPI(context, lyDataBean.getAppid());
            if (!iwxapi.registerApp(lyDataBean.getAppid())) {
                Toast.makeText(context, R.string.failure_pay_weixin, Toast.LENGTH_SHORT);
                return;
            }
        }
        Wx_APP_ID = lyDataBean.getAppid();
        PayReq request = new PayReq();
        request.appId = lyDataBean.getAppid();
        request.partnerId = lyDataBean.getPartnerid();
        request.prepayId = lyDataBean.getPrepayid();
        request.packageValue = lyDataBean.getPackageX();
        request.nonceStr = lyDataBean.getNoncestr();
        request.sign = lyDataBean.getSign();
        request.timeStamp = lyDataBean.getTimestamp();
        iwxapi.sendReq(request);
       /* Logger.e("Wu","是否注册成功的数据 = appid ="+request.appId+" partnerId="+request.partnerId
        +" prepayId="+request.prepayId+" packageValue="+request.packageValue+" nonceStr="+request.nonceStr
                +" sign="+request.sign+" timeStamp="+request.timeStamp);*/
    }




}
