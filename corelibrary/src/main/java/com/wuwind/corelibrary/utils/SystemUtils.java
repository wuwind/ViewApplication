package com.wuwind.corelibrary.utils;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * Created by Wuhf on 2016/5/19.
 * Description ：
 */
public class SystemUtils {

    /**
     * 获取当前运行的activity名称
     * @param context
     * @return
     */
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

    /**
     * 获取当前运行的应用的包名
     * @param context
     * @return
     */
    public static String getTopPackageName(Context context) {
        String topPackageName = null;
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topPackageName = f.getPackageName();
        }
        return topPackageName;
    }

    /**
     * 获取已安装的包信息
     * @param context
     * @return
     */
    public static  List<PackageInfo> getInstallInfos(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        return installedPackages;
    }

    /**
     * 服务是否在运行
     * @param context
     * @param serviceClassName
     * @return
     */
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

    /**
     * 判断设备是否是模拟器
     *
     * @return
     */
    public static boolean isEmulator() {
        return "sdk".equals(Build.PRODUCT) || "google_sdk".equals(Build.PRODUCT) ||
                "generic".equals(Build.BRAND.toLowerCase(Locale.getDefault()));
    }

    /**
     * 复制到剪贴板
     * @param context
     * @param text
     */
    public static void copyToClipboard(Context context, String text) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
        }
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static Integer getVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
            if (versioncode > 0) {
                return versioncode;
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    /**
     * 获取版本名称
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 获取设备唯一号
     * @param context
     * @return
     */
    public static String getMyUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = null, tmSerial = null, tmPhone, androidId;
        try {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        if (tmDevice != null) {
            return tmDevice;
        } else if (tmSerial != null) {
            return tmSerial;
        } else if (androidId != null) {
            return androidId;
        }
        return tmDevice;
    }
}
