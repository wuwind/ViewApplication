package com.wuwind.corelibrary.network;

import com.wuwind.corelibrary.base.BaseApplication;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Wuhf on 2016/3/24.
 * Description ：
 */
public class ObservableFactory {

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
//    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxCacheCallAdapterFactory.create(BasicCache.fromCtx(BaseApplication.context));

    public static <T> T createFrom(Class<T> classServer) {
        return createFrom(BaseApplication.baseUrl, classServer);
    }

    public static <T> T createFrom(String baseUrl, Class<T> classServer) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)//添加Rx适配器
                .addConverterFactory(gsonConverterFactory)//添加Gson转换器
                .build();

        return retrofit.create(classServer);

    }

}
