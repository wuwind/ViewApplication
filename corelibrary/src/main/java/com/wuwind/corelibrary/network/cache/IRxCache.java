package com.wuwind.corelibrary.network.cache;


import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;

public interface IRxCache {

    /**
     * 添加到缓存
     * @param request
     * @param buffer
     */
    void addInCache(Request request, Buffer buffer);

    /**
     * 从缓存获取
     * @param request
     * @return
     */
    ResponseBody getFromCache(Request request);

}
