package com.audiolaby.controller.service;

import com.audiolaby.controller.model.Pagination;
import com.audiolaby.controller.model.SearchPagination;
import com.audiolaby.controller.request.CommentsRequest;
import com.audiolaby.controller.request.RatingRequest;
import com.audiolaby.controller.request.SaveCommentRequest;
import com.audiolaby.controller.request.PostDetailsRequest;
import com.audiolaby.controller.response.CommentsListResponse;
import com.audiolaby.controller.response.CommonResponse;
import com.audiolaby.controller.response.PostDetailsResponse;
import com.audiolaby.controller.response.PostsListResponse;
import com.audiolaby.controller.response.SaveRatingResponse;
import com.audiolaby.controller.response.SearchResponse;
import com.audiolaby.controller.response.UpdateWishlistResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AudioService {

    @POST("get-post")
    Call<PostDetailsResponse> postDetails(@Body PostDetailsRequest postDetailsRequest);

    @POST("save-view")
    Call<CommonResponse> saveListning(@Body PostDetailsRequest postDetailsRequest);

    @POST("save-download")
    Call<CommonResponse> saveDownload(@Body PostDetailsRequest postDetailsRequest);


    @POST("save-rate")
    Call<SaveRatingResponse> saveRating(@Body RatingRequest ratingRequest);


    @POST("save-comment")
    Call<CommentsListResponse> saveComment(@Body SaveCommentRequest postDetailsRequest);


    @POST("get-comments")
    Call<CommentsListResponse> comments(@Body CommentsRequest commentsRequest);



    @POST("get-posts/{type}")
    Call<PostsListResponse> posts(@Path("type") String type, @Body Pagination pagination);


    @POST("update-wishlist")
    Call<UpdateWishlistResponse> updateWishlist(@Body PostDetailsRequest postDetailsRequest);

    @POST("get-wishlist")
    Call<PostsListResponse> wishlist(@Body Pagination pagination);

    @POST("global-search")
    Call<SearchResponse> searchWord(@Body SearchPagination searchPagination);

}
