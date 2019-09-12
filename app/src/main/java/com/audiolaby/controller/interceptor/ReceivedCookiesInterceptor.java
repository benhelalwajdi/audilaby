package com.audiolaby.controller.interceptor;

import com.audiolaby.Audiolaby;
import com.audiolaby.persistence.AppPref_;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

@EBean(scope = Scope.Singleton)
public class ReceivedCookiesInterceptor implements Interceptor {

    @Pref
    AppPref_ appPref;


    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (originalResponse.header("token")!=null && !originalResponse.header("token").isEmpty()) {
            appPref.token().put(originalResponse.header("token"));
        }
        return originalResponse;
    }
}
