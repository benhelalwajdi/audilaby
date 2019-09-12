package com.audiolaby.controller.interceptor;

import android.content.Context;

import com.audiolaby.Audiolaby;
import com.audiolaby.persistence.AppPref_;
import com.audiolaby.util.ConnectivityUtils;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@EBean(scope = Scope.Singleton)
public class OfflineInterceptor implements Interceptor {
    @App
    Audiolaby audiolaby;
    Context mContext;
    @Pref
    AppPref_ appPref;

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!ConnectivityUtils.isConnected(mContext)) {
//            CacheControl cacheControl = new CacheControl.Builder()
//                    .maxStale(udraeConfig.getOfflineCacheDuration(), TimeUnit.DAYS)
//                    .build();
//
//            request = request.newBuilder()
//                    .cacheControl(cacheControl)
//                    .build();


        }


//
//        if (NetworkUtility.isNetworkAvailable(MyApplication.getInstance())) {
//            int maxAge = 60;
//            // request = request.newBuilder().header("Cache-Control", "public, max-age=" + maxAge).build();
//            CacheControl cacheControl = new CacheControl.Builder()
//                    .maxAge(1, TimeUnit.MINUTES)
//                    .noCache()
//                    .build();
//
//            request.newBuilder()
//                    .cacheControl(cacheControl)
//                    .build();
//        } else {
//            int maxStale = 60 * 60 * 24 * 7; // 1 week
//            // request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).build();
//            CacheControl cacheControl = new CacheControl.Builder()
//                    .onlyIfCached()
//                    .maxStale(7, TimeUnit.DAYS)
//                    .build();
//
//            request.newBuilder()
//                    .cacheControl(cacheControl)
//                    .build();
//        }

        return chain.proceed(request);
    }
}
