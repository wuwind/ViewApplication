package com.wuwind.corelibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;


public class PackageUtil {

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

    public static String getMyUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = null, tmSerial = null, tmPhone, androidId;
        try {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
        } catch (Exception e) {
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
