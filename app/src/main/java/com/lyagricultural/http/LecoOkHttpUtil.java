package com.lyagricultural.http;

import android.util.Log;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;


/**
 * 哪果要在onDestroy清除请求，那就要new一个实例
 * Created by miles on 2016/5/23.
 */
public class LecoOkHttpUtil extends OkHttpUtils {
    private static final String TAG = "LecoOkHttpUtil";
    private static final int CODE_SESSION_FAILED = -201;

    public interface OnSessionFailedListener {
        public void onSessionFailed();
    }

    private static OkHttpClient mOkHttpClient;

    private OnSessionFailedListener mSessionFailedlistener;
    private List<RequestCall> mRunningCalls = new ArrayList<>();

    public LecoOkHttpUtil() {
        super(mOkHttpClient);
        mOkHttpClient = getOkHttpClient();
    }

    @Override
    public void execute(final RequestCall requestCall, final com.zhy.http.okhttp.callback.Callback callback) {
        //MLog.i("execute..." + mRunningCalls.size());
        /*if(!LecoUtils.isNetworkAvailable(mContext)) {
           // MLog.i("execute... network is not available..");
            Toast.makeText(mContext, mContext.getString(R.string.network_is_not_available), Toast.LENGTH_SHORT).show();
            return;
        }*/
        mRunningCalls.add(requestCall);
       // MLog.i("execute..." + mRunningCalls.size());
        super.execute(requestCall, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                //请求完成，删除请求
                removeRequestCall(requestCall);
                callback.onError(call, e);
            }

            @Override
            public void onResponse(String response) {
                //请求完成，删除请求
                removeRequestCall(requestCall);
                try{
                    JSONObject json = new JSONObject();
                    if (json.has("code")) {
                        int code = json.getInt("code");
                        if (code == CODE_SESSION_FAILED) {
                            onSessionFailed();
                            return;
                        }
                    }
                } catch (Exception e) {
                    //Toast.makeText(mContext, mContext.getString(R.string.json_parse_error), Toast.LENGTH_SHORT).show();
                }

                callback.onResponse(response);
            }
        });
    }


    private void removeRequestCall(RequestCall call) {
        if (mRunningCalls.contains(call)) {
            mRunningCalls.remove(call);
        }
    }

    /**
     * 在activity的onDestroy中调用，用于终止当前activity的http请求
     * clean up calls on Activity destroyed;
     */
    public void onDestroy() {
        //MLog.i("onDestroy ... clean mRunningCalls.size()  " + mRunningCalls.size());
        if (mRunningCalls.size() > 0) {
            for (RequestCall call : mRunningCalls) {
                //MLog.i("onDestroy ... clean RequestCalls call " + call);
                call.cancel();
            }

        }
        mRunningCalls.clear();
    }

    private void onSessionFailed() {
        //callback自己处理
        /*if(mSessionFailedlistener != null) {
            mSessionFailedlistener.onSessionFailed();
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
        }*/
    }

    public void setSessionFailedlistener(OnSessionFailedListener listener) {
        mSessionFailedlistener = listener;
    }
}
