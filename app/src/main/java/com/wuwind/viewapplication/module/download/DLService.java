package com.wuwind.viewapplication.module.download;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.wuwind.corelibrary.network.download.DLManager;
import com.wuwind.corelibrary.network.download.interfaces.SimpleDListener;
import com.wuwind.viewapplication.R;

import java.io.File;

public class DLService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        String path = intent.getStringExtra("path");
        final int id = intent.getIntExtra("id", -1);
        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        final int[] length = new int[1];
        DLManager.getInstance(this).dlStart(url, path, null, null, new SimpleDListener() {
            @Override
            public void onStart(String fileName, String realUrl, int fileLength) {
                builder.setContentTitle(fileName);
                length[0] = fileLength;
            }

            @Override
            public void onProgress(int progress) {
                builder.setProgress(length[0], progress, false);
                nm.notify(id, builder.build());
            }

            @Override
            public void onFinish(File file) {
                nm.cancel(id);
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
