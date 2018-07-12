package com.lyagricultural.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.lyagricultural.utils.LyLog;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/6/1.
 */
public class JpushReceiver extends BroadcastReceiver{
    private static final String TAG = "JpushReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LyLog.d(TAG,"[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
        }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

            LyLog.d(TAG,"[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LyLog.d(TAG,"[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//            if (LoginActivity.isOpen){
//
//            }
//            else {
//                Utils.saveMsgCount("message",Utils.getMsgCount("message")+1);
//                ShortcutBadger.applyCount(BabyApplication.context,Utils.getMsgCount("message"));//接收到推送消息，改变角标
//            }

        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LyLog.d(TAG,"[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            //receivingNotification(context,bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

//             Utils.saveMsgCount("message",0);
//             ShortcutBadger.applyCount(BabyApplication.context,Utils.getMsgCount("message"));
//            LogUtil.d("[MyReceiver] 用户点击打开了通知");
//            String type;
//            String id = null;
//            try {
//                JSONObject jsonObject = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
//                type = jsonObject.optString("type");
//                id = jsonObject.getString("id");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
           LyLog.d(TAG,"[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LyLog.d(TAG, "[MyReceiver]" + intent.getAction() +" connected:"+connected);

        } else {
            LyLog.d(TAG,"[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }
}
