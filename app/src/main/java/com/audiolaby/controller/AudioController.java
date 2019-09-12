package com.audiolaby.controller;

import com.activeandroid.Model;
import com.activeandroid.util.Log;
import com.audiolaby.controller.interceptor.AddCookiesInterceptor;
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
import com.audiolaby.controller.service.AudioService;
import com.audiolaby.persistence.LibraryDAO;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EBean(scope = Scope.Singleton)
public class AudioController {
    public static final String BASE_URL;
    @Bean
    AddCookiesInterceptor addCookiesInterceptor;
    @Bean
    LibraryDAO libraryDAO;
    private AudioService audioService;
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

        this.audioService = (AudioService) this.retrofit.create(AudioService.class);
    }



    public PostDetailsResponse postDetails(PostDetailsRequest postDetailsRequest) {
        PostDetailsResponse body = null;
        try {
            body = this.audioService.postDetails(postDetailsRequest).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new PostDetailsResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }

    public CommonResponse saveListning (PostDetailsRequest postDetailsRequest) {
        CommonResponse body = null;
        try {
            body = this.audioService.saveListning(postDetailsRequest).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new CommonResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }

    public CommonResponse saveDownload (PostDetailsRequest postDetailsRequest) {
        CommonResponse body = null;
        try {
            body = this.audioService.saveDownload(postDetailsRequest).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new CommonResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }


    public SaveRatingResponse saveRating (RatingRequest ratingRequest) {
        SaveRatingResponse body = null;
        try {
            body = this.audioService.saveRating(ratingRequest).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new SaveRatingResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }

    public CommentsListResponse saveComments (SaveCommentRequest commentRequest) {
        CommentsListResponse body = null;
        try {
            body = this.audioService.saveComment(commentRequest).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new CommentsListResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }

    public CommentsListResponse comments (CommentsRequest commentsRequest) {
        CommentsListResponse body = null;
        try {
            body = this.audioService.comments(commentsRequest).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new CommentsListResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }

    public PostsListResponse posts(String type, Pagination pagination) {
        PostsListResponse body = null;
        try {
            if (libraryDAO!= null && libraryDAO.getUser() != null)
                pagination.setUser_id(libraryDAO.getUser().getUser_id());
            body = this.audioService.posts(type,pagination).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new PostsListResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }

    public UpdateWishlistResponse updateWishlist (PostDetailsRequest postDetailsRequest) {
        UpdateWishlistResponse body = null;
        try {
            body = this.audioService.updateWishlist(postDetailsRequest).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new UpdateWishlistResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }

    public PostsListResponse wishlist(Pagination pagination) {
        PostsListResponse body = null;
        try {
            body = this.audioService.wishlist(pagination).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new PostsListResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }


    public SearchResponse searchWord(SearchPagination searchPagination) {
        SearchResponse body = null;
        try {
            body = this.audioService.searchWord(searchPagination).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new SearchResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }
}
