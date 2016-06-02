package com.wuwind.corelibrary.utils;

import android.graphics.Bitmap;

/**
 * Created by Wuhf on 2016/5/6.
 * Description ：
 */
public interface IBitmapCache {

    void addToCache(String key, Bitmap bitmap);

    Bitmap getFromCache(String key);
}
