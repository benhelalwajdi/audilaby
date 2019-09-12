package com.audiolaby.controller.service;

import com.audiolaby.controller.request.SectionItemsRequest;
import com.audiolaby.controller.response.CategoryListResponse;
import com.audiolaby.controller.response.CoversResponse;
import com.audiolaby.controller.response.SectionItemResponse;
import com.audiolaby.controller.response.SectionListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CatalogService {
    @POST("get-categories")
    Call<CategoryListResponse> categories();

    @POST("get-sections")
    Call<SectionListResponse> sections();

    @POST("get-sections-posts")
    Call<SectionItemResponse> sectionItems(@Body SectionItemsRequest sectionItemsRequest);

    @POST("get-covers")
    Call<CoversResponse> covers();
//
//    @POST("getAudiobookList")
//    Call<GetAudiobookListResponse> getAudiobookList(@Body GetAudiobookListRequest getAudiobookListRequest);
//
//    @POST("getAudiobookReviewList")
//    Call<GetAudiobookReviewListResponse> getAudiobookReviewList(@Body GetAudiobookReviewListRequest getAudiobookReviewListRequest);
//
//    @POST("getAuthor")
//    Call<GetAuthorResponse> getAuthor(@Body GetAuthorRequest getAuthorRequest);
//
//    @POST("getFilterGenreList")
//    Call<GetGenreListResponse> getGenreList();
//
//    @POST("getSectionTitle")
//    Call<GetSectionResponse> getSectionTitle(@Body GetSectionRequest getSectionRequest);
//
//    @POST("getSectionItemList")
//    Call<GetSectionItemListResponse> getSectionItemList(@Body GetSectionItemListRequest getSectionItemListRequest);
//
//    @POST("getSectionList")
//    Call<GetSectionListResponse> getSectionList(@Body GetSectionListRequest getSectionListRequest);
//
//    @POST("getSubGenreList")
//    Call<GetGenreListResponse> getSubgenreList(@Body GetSubgenreListRequest getSubgenreListRequest);
}
