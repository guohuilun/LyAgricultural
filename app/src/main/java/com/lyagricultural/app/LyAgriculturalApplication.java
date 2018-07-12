package com.lyagricultural.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.lyagricultural.gen.DaoMaster;
import com.lyagricultural.gen.DaoSession;
import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport;
import com.tongguan.yuanjian.family.Utils.PersonManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 作者Administrator on 2018/5/22 0022 09:15
 */
public class LyAgriculturalApplication  extends Application{
    public ActivityManager activityManager;

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        activityManager = ActivityManager.getInstance();
        initView();
    }

    private void initView(){
//      极光推送
/*        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;//设置为自动消失
        builder.notificationDefaults =
                Notification.DEFAULT_LIGHTS;
        JPushInterface.setPushNotificationBuilder(1, builder);*/
//Mob分享 注册
        MobSDK.init(this);

//配置数据库
        setUpDatabase();

//腾讯bugly初始化
        Context context = getApplicationContext();
// 获取当前包名
        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
// 初始化Bugly  建议在测试阶段建议设置成true，发布时设置为false.
        CrashReport.initCrashReport(context, "88d85cf50c", true, strategy);

// 初始化在线视频
        PersonManager.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
        //在内存较低的时候执行，这个方法可以执行一些清理内存的操作
    }

    /**
     * 创建数据库
     */
     private void setUpDatabase(){

//         DevOpenHelper：创建SQLite数据库的SQLiteOpenHelper的具体实现
//
//         DaoMaster：GreenDao的顶级对象，作为数据库对象、用于创建表和删除表
//
//         DaoSession：管理所有的Dao对象，Dao对象中存在着增删改查等API

//         创建数据库shop.db
         DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(this,"shop.db",null);
//         获取可写数据库
         SQLiteDatabase db = helper.getWritableDatabase();
//         获取数据库对象
         DaoMaster daoMaster=new DaoMaster(db);
         daoSession= daoMaster.newSession();

     }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}

