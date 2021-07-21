package com.jpkh.cnpc.activity.interfaces;

import com.jpkh.cnpc.protocol.constants.SysConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/13 0013.
 * Retrofit+RxJava网络请求框架
 */
public class HttpUtils {

    private static HttpUtils instance;
    private Retrofit retrofit;

    private HttpUtils() {
        this.instance = this;
        this.retrofit = new Retrofit.Builder()
                .baseUrl(SysConfig.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    return new HttpUtils();
                }
            }
        }
        return instance;
    }

    public static <T> T getService(Class<T> c) {
        return getInstance().retrofit.create(c);
    }

    public static <T> void init(Observable<T> observable, Subscriber<T> subscriber) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
