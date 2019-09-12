package com.audiolaby.controller.interceptor;

import com.audiolaby.Audiolaby;
import com.audiolaby.persistence.AppPref_;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request.Builder;
import okhttp3.Response;

@EBean(scope = Scope.Singleton)
public class AddCookiesInterceptor implements Interceptor {
    @App
    Audiolaby audiolaby;


    @Pref
    AppPref_ appPref;

    public Response intercept(Chain chain) throws IOException {
        Builder builder = chain.request().newBuilder();
        builder.addHeader("Authorization", appPref.token().get());
        return chain.proceed(builder.build());

    }

}
