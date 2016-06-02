package com.wuwind.corelibrary.network;

import okhttp3.Interceptor;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Wuhf on 2016/3/24.
 * Description ：
 */
public class BaseHttpClient {

//    private static GetOrderApi getOrder = ObservableFactory.createFrom(GetOrderApi.class);

    protected static Subscription subNext(Observable observable, Observer observer) {
        return  observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private static Subscription getOrder(Observer observer) {
        Observable orderObservable = null;
        return subNext(orderObservable, observer);
    }



}
