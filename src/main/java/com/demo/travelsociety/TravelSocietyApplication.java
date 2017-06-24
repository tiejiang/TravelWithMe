package com.demo.travelsociety;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.demo.travelsociety.common.CCPAppManager;
import com.demo.travelsociety.db.DBManager;


/**
 *
 */

public class TravelSocietyApplication extends Application{
    private DBManager dbHelper;
    private static Application instance;
    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        instance = this;
        SDKInitializer.initialize(getApplicationContext());
        CCPAppManager.setContext(instance);
        dbHelper = new DBManager(this);
        dbHelper.openDatabase();
    }
    /**
     * 单例，返回一个实例
     * @return
     */
    public static Application getInstance() {
        if (instance == null) {
            Log.d("TIEJIANG", "[Application] instance is null.");
        }
        Log.d("TIEJIANG", "[ECApplication] return instance succeed.");
        return instance;
    }
    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }
}
