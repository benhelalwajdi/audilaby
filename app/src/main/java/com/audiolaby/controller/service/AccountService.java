package com.audiolaby.controller.service;

import com.audiolaby.controller.request.ConfirmCodeRequest;
import com.audiolaby.controller.request.ForgetPasswordRequest;
import com.audiolaby.controller.request.GetUserIdRequest;
import com.audiolaby.controller.request.ResetPasswordRequest;
import com.audiolaby.controller.request.SavePaidAudioRequest;
import com.audiolaby.controller.request.SendResetPasswordRequest;
import com.audiolaby.controller.request.SignInRequest;
import com.audiolaby.controller.request.SignInSocialRequest;
import com.audiolaby.controller.request.VerifRequest;
import com.audiolaby.controller.response.AccountResponse;
import com.audiolaby.controller.response.CommonResponse;
import com.audiolaby.controller.response.SaveOrderResponse;
import com.audiolaby.controller.response.VerifResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AccountService {

    @POST("get-user-details")
    Call<AccountResponse> getUserDetails();


    @POST("verif-app-id")
    Call<VerifResponse> verifUser(@Body VerifRequest verifRequest);

    @POST("get-user-details")
    Call<AccountResponse> getUserDetails(@Body GetUserIdRequest getUserIdRequest);

    @POST("sendResetPassword")
    Call<AccountResponse> sendResetPassword(@Body SendResetPasswordRequest sendResetPasswordRequest);

    @POST("login")
    Call<AccountResponse> signIn(@Body SignInRequest signRequest);

    @POST("login")
    Call<AccountResponse> signInSocial(@Body SignInSocialRequest signInSocialRequest);


    @POST("send-code-reset")
    Call<CommonResponse> sendCode(@Body ForgetPasswordRequest forgetPasswordRequest);


    @POST("verif-code-reset")
    Call<CommonResponse> confirmCode(@Body ConfirmCodeRequest confirmCodeRequest);


    @POST("reset-password")
    Call<CommonResponse> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);


    @POST("signOut")
    Call<AccountResponse> signOut();


    @POST("signup")
    @Multipart
    Call<AccountResponse> signUp(@Part("loginType") RequestBody loginType,
                                 @Part("email") RequestBody email,
                                 @Part("password") RequestBody password,
                                 @Part("firstName") RequestBody firstName,
                                 @Part("lastName") RequestBody lastName,
                                 @Part("country") RequestBody country,
                                 @Part("fcm_id") RequestBody fcm_id,
                                 @Part("device_os") RequestBody device_os,
                                 @Part("device_version") RequestBody device_version,
                                 @Part("app_version") RequestBody app_version,
                                 @Part("lastSeen") RequestBody lastSeen,
                                 @Part("gender") RequestBody gender,
                                 @Part("birthday") RequestBody birthday,
                                 @Part MultipartBody.Part part);

    @POST("signup")
    @Multipart
    Call<AccountResponse> signUp(@Part("loginType") RequestBody loginType,
                                 @Part("email") RequestBody email,
                                 @Part("password") RequestBody password,
                                 @Part("firstName") RequestBody firstName,
                                 @Part("lastName") RequestBody lastName,
                                 @Part("country") RequestBody country,
                                 @Part("fcm_id") RequestBody fcm_id,
                                 @Part("device_os") RequestBody device_os,
                                 @Part("device_version") RequestBody device_version,
                                 @Part("app_version") RequestBody app_version,
                                 @Part("lastSeen") RequestBody lastSeen,
                                 @Part("gender") RequestBody gender,
                                 @Part("birthday") RequestBody birthday);


    @POST("signup")
    @Multipart
    Call<AccountResponse> signUpSocial(@Part("loginType") RequestBody loginType,
                                       @Part("email") RequestBody email,
                                       @Part("socialId") RequestBody socialId,
                                       @Part("firstName") RequestBody firstName,
                                       @Part("lastName") RequestBody lastName,
                                       @Part("country") RequestBody country,
                                       @Part("fcm_id") RequestBody fcm_id,
                                       @Part("device_os") RequestBody device_os,
                                       @Part("device_version") RequestBody device_version,
                                       @Part("app_version") RequestBody app_version,
                                       @Part("lastSeen") RequestBody lastSeen,
                                       @Part("gender") RequestBody gender,
                                       @Part("birthday") RequestBody birthday,
                                       @Part MultipartBody.Part part);


    @POST("signup")
    @Multipart
    Call<AccountResponse> signUpSocial(@Part("loginType") RequestBody loginType,
                                       @Part("email") RequestBody email,
                                       @Part("socialId") RequestBody socialId,
                                       @Part("firstName") RequestBody firstName,
                                       @Part("lastName") RequestBody lastName,
                                       @Part("country") RequestBody country,
                                       @Part("fcm_id") RequestBody fcm_id,
                                       @Part("device_os") RequestBody device_os,
                                       @Part("device_version") RequestBody device_version,
                                       @Part("app_version") RequestBody app_version,
                                       @Part("lastSeen") RequestBody lastSeen,
                                       @Part("gender") RequestBody gender,
                                       @Part("birthday") RequestBody birthday);

    @POST("json")
    Call<AccountResponse> getCountry();




    @POST("save/order")
    Call<SaveOrderResponse> savePaidAudio(@Body SavePaidAudioRequest savePaidAudioRequest);




}