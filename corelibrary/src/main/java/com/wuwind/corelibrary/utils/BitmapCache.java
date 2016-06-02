package com.wuwind.corelibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.wuwind.corelibrary.base.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by Wuhf on 2016/5/6.
 * Description ：
 */
public class BitmapCache implements IBitmapCache {

    private DiskLruCache diskLruCache;
    private LruCache<String, Bitmap> memCache;

    private BitmapCache(File cacheFile, long maxDiskSize, int maxMemSize) {
        try {
            diskLruCache = DiskLruCache.open(cacheFile, PackageUtil.getVersionCode(BaseApplication.context), 1, maxDiskSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        memCache = new LruCache<>(maxMemSize);
    }

    private static final int REASONABLE_DISK_SIZE = (int) (Runtime.getRuntime().maxMemory() / 3);
    private static final int REASONABLE_MEM_ENTRIES = 100 * 1024 * 1024;

    public static BitmapCache create() {
        File file = null;
        try {
            file = new File(FilePathUtil.getDiskCacheDir(), "bitmapCache");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BitmapCache(file, REASONABLE_DISK_SIZE, REASONABLE_MEM_ENTRIES);
    }


    @Override
    public void addToCache(String key, Bitmap bitmap) {
        key = Md5.Md5(key);
        LogUtil.e("add memCache");
        memCache.put(key, bitmap);
        try {
            LogUtil.e("add diskLruCache");
            DiskLruCache.Editor edit = diskLruCache.edit(key);
            if (null == edit) {
                LogUtil.e("diskLruCache save error");
                return;
            }
            OutputStream outputStream = edit.newOutputStream(0);
            boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            if (success)
                edit.commit();
            else
                edit.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getFromCache(String key) {
        key = Md5.Md5(key);
        Bitmap bitmap = memCache.get(key);
        if (null != bitmap) {
            LogUtil.e("get from memCache");
            return bitmap;
        }
        try {
            LogUtil.e("get from diskLruCache");
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (null == snapshot)
                return null;
            InputStream inputStream = snapshot.getInputStream(0);
            bitmap = BitmapFactory.decodeStream(inputStream);
            memCache.put(key, bitmap);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
