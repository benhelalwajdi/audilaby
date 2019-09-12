package com.audiolaby.controller;

import com.activeandroid.util.Log;
import com.audiolaby.controller.interceptor.AddCookiesInterceptor;
import com.audiolaby.controller.request.AutocompleteStoreRequest;
import com.audiolaby.controller.request.SearchStoreRequest;
import com.audiolaby.controller.response.AutocompleteSearchResponse;
import com.audiolaby.controller.response.SearchStoreResponse;
import com.audiolaby.controller.service.SearchService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@EBean(scope = Scope.Singleton)
public class SearchController {
    public static final String BASE_URL;
    @Bean
    AddCookiesInterceptor addCookiesInterceptor;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private SearchService searchService;

    static {
        BASE_URL = ServerAddresses.BASE_REST.getValue();
    }

    @AfterInject
    public void afterInjection() {
        this.okHttpClient = new Builder().addInterceptor(this.addCookiesInterceptor).build();
        this.retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(JacksonConverterFactory.create()).client(this.okHttpClient).build();
        this.searchService = (SearchService) this.retrofit.create(SearchService.class);
    }

    public SearchStoreResponse searchStore(SearchStoreRequest searchStoreRequest) {
        SearchStoreResponse body = null;
        try {
            Response<SearchStoreResponse> response = this.searchService.searchStore(searchStoreRequest).execute();
            body = (SearchStoreResponse) response.body();
            if (response.errorBody() != null) {
                Log.i("eStories-errors", response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    public AutocompleteSearchResponse autocompleteStore(AutocompleteStoreRequest autocompleteStoreRequest) {
        AutocompleteSearchResponse body = null;
        try {
            Response<AutocompleteSearchResponse> response = this.searchService.autocompleteStore(autocompleteStoreRequest).execute();
            body = (AutocompleteSearchResponse) response.body();
            if (response.errorBody() != null) {
                Log.i("eStories-errors", response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
}
