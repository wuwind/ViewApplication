package com.wuwind.corelibrary.base;

import android.app.Application;
import android.content.Context;

import com.wuwind.corelibrary.utils.CrashHandler;
import com.wuwind.corelibrary.utils.NetUtil;
import com.wuwind.corelibrary.utils.SystemUtils;


public abstract class CoreApplication extends Application {

    public static String uid;
    public static Context context;
    public static boolean networkAvailable;
    public static int versionCode;
    public static String baseUrl;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        uid = SystemUtils.getMyUUID(context);
        networkAvailable = NetUtil.isNetworkConnected(context);
        versionCode = SystemUtils.getVersionCode(this);
        init();
    }

    protected abstract void init();

    protected void initCrashHandler() {
        new CrashHandler().init();
    }

}