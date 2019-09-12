package com.audiolaby.controller;

import com.activeandroid.Model;
import com.activeandroid.util.Log;
import com.audiolaby.controller.interceptor.AddCookiesInterceptor;
import com.audiolaby.controller.request.SectionItemsRequest;
import com.audiolaby.controller.response.CategoryListResponse;
import com.audiolaby.controller.response.CoversResponse;
import com.audiolaby.controller.response.SectionItemResponse;
import com.audiolaby.controller.response.SectionListResponse;
import com.audiolaby.controller.service.CatalogService;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import java.io.IOException;
import java.io.InterruptedIOException;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@EBean(scope = Scope.Singleton)
public class CatalogController {
    public static final String BASE_URL;
    @Bean
    AddCookiesInterceptor addCookiesInterceptor;
    private CatalogService catalogService;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    static {
        BASE_URL = ServerAddresses.BASE_REST.getValue();
    }

    @AfterInject
    public void afterInjection() {
        this.okHttpClient = new Builder().addInterceptor(this.addCookiesInterceptor).build();

        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(Model.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(this.okHttpClient).build();

        this.catalogService = (CatalogService) this.retrofit.create(CatalogService.class);
    }


    public CategoryListResponse categories() {
        CategoryListResponse body = null;
        try {
            body = this.catalogService.categories().execute().body();;

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new CategoryListResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }

    public CoversResponse covers() {
        CoversResponse body = null;
        try {
            body = this.catalogService.covers().execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new CoversResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }

    public SectionListResponse sections() {
        SectionListResponse body = null;
        try {
            body = this.catalogService.sections().execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new SectionListResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }

    public SectionItemResponse sectionItems(SectionItemsRequest sectionItemsRequest) {
        SectionItemResponse body = null;
        try {
            body = this.catalogService.sectionItems(sectionItemsRequest).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new SectionItemResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }
}
