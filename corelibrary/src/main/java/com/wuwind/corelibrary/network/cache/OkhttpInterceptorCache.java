package com.wuwind.corelibrary.network.cache;

import com.wuwind.corelibrary.base.BaseApplication;
import com.wuwind.corelibrary.utils.FilePathUtil;
import com.wuwind.corelibrary.utils.LogUtil;
import com.wuwind.corelibrary.utils.NetUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Wuhf on 2016/5/5.
 * Description ：
 */
public class OkhttpInterceptorCache {

    public static OkHttpClient getInstance() {
        OkHttpClient okHttpClient = null;
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetUtil.isConnect(BaseApplication.context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                    LogUtil.e("no network");
                }

                Response response = chain.proceed(request);

                if (NetUtil.isConnect(BaseApplication.context)) {
                    int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
                    LogUtil.e("has network maxAge=" + maxAge);
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    LogUtil.e("network error");
                    int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                    LogUtil.e("has maxStale=" + maxStale);
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                    LogUtil.e("response build maxStale=" + maxStale);
                }
                return response;
            }
        };

        try {
            File httpCacheDirectory = new File(FilePathUtil.getCachePath(), "response");
            Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .cache(cache)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return okHttpClient;
    }

}
