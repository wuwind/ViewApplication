package com.wuwind.corelibrary.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by Wuhf on 2016/5/19.
 * Description ：
 */
public class SystemUtils {

    public static String getTopActivityName(Context context) {
        String topActivityName = null;
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityName = f.getClassName();
        }
        return topActivityName;
    }

    public static String getTopPackageName(Context context) {
        String topActivityName = null;
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityName = f.getPackageName();
        }
        return topActivityName;
    }

    public static void addWindowView(Context context, int x, int y, View view) {
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = x;
        params.y = y;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        windowManager.addView(view, params);
    }

    public static void getInstallInfos(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo info : installedPackages) {
            Log.e("getInstallInfos----: ", info.packageName + "");
        }
    }

    public static boolean isRunningService(Context context, String serviceClassName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : runningServices) {
            if(info.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
}
