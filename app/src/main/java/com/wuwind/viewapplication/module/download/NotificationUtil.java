package com.wuwind.viewapplication.module.download;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public final class NotificationUtil {
    public static void notificationForDLAPK(Context context, String url) {
        notificationForDLAPK(context, url, Environment.getExternalStorageDirectory() + "/AigeStudio/");
    }

    public static void notificationForDLAPK(Context context, String url, String path) {
        Intent intent = new Intent(context, DLService.class);
        intent.putExtra("url", url);
        intent.putExtra("path", path);
        intent.putExtra("id", (int) (Math.random() * 1024));
        context.startService(intent);
    }
}
