package com.audiolaby.controller.service;

import com.audiolaby.controller.request.AutocompleteStoreRequest;
import com.audiolaby.controller.request.SearchStoreRequest;
import com.audiolaby.controller.response.AutocompleteSearchResponse;
import com.audiolaby.controller.response.SearchStoreResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SearchService {
    @POST("autocompleteStore")
    Call<AutocompleteSearchResponse> autocompleteStore(@Body AutocompleteStoreRequest autocompleteStoreRequest);

    @POST("searchStore")
    Call<SearchStoreResponse> searchStore(@Body SearchStoreRequest searchStoreRequest);
}
