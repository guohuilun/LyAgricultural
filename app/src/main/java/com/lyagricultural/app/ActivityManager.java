package com.lyagricultural.app;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.util.Stack;

/**
 * Created by admin on 2016/10/13.
 */
public class ActivityManager {

    /**
     * 用于保存Activity实例
     */
    public Stack<Activity> stack = new Stack<>();
    private static final ActivityManager activityManager = new ActivityManager();

    private static boolean isExit = false;

    /**
     * 单例
     *
     * @return
     */
    public static ActivityManager getInstance() {
        return activityManager;
    }

    /**
     * 将Activity移除栈
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            stack.remove(activity);
        }
    }

    /**
     * Activity入栈
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (activity != null) {
            stack.push(activity);
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void endActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            stack.remove(activity);
        }
    }

    /**
     * 获取最上层的Activity
     *
     * @return
     */
    public Activity currentActivity() {
        if (!stack.isEmpty()) {
            return stack.lastElement();
        }
        return null;
    }

    /**
     * 移除除了cls外所有的Activity
     *
     * @param cls
     */
    public void popAllActivityExceptTarget(Class<? extends Activity> cls) {
        if (cls != null) {
            while (true) {
                Activity activity = currentActivity();
                if (activity == null) {
                    break;
                }
                if (activity.getClass().equals(cls)) {
                    break;
                }
                endActivity(activity);
            }
        }
    }

    /**
     * 结束除cls之外的所有activity,执行结果都会清空Stack
     *
     * @param cls
     */
    public void clearAllActivityExceptTarget(Class<? extends Activity> cls) {
        Activity restActivity = null;
        while (!stack.empty()) {
            Activity activity = currentActivity();
            if (activity != null && activity.getClass().equals(cls)) {
                popActivity(activity);
                restActivity = activity;
            } else {
                endActivity(activity);
            }
        }
        pushActivity(restActivity);
    }


    /**
     * 结束所有activity
     */
    public void finishAllActivity() {
        if (!isExit) {
            isExit = true;
            mHamdler.sendEmptyMessageDelayed(1001, 2000);
        }else {
            while (!stack.empty()) {
                Activity activity = currentActivity();
                endActivity(activity);
            }
        }
    }

    Handler mHamdler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

}
