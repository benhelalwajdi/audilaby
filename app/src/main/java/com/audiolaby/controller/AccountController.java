package com.audiolaby.controller;

import android.content.Context;
import android.support.annotation.NonNull;

import com.activeandroid.Model;
import com.activeandroid.util.Log;
import com.audiolaby.Audiolaby;
import com.audiolaby.R;
import com.audiolaby.controller.interceptor.AddCookiesInterceptor;
import com.audiolaby.controller.interceptor.ReceivedCookiesInterceptor;
import com.audiolaby.controller.request.ConfirmCodeRequest;
import com.audiolaby.controller.request.ForgetPasswordRequest;
import com.audiolaby.controller.request.ResetPasswordRequest;
import com.audiolaby.controller.request.SavePaidAudioRequest;
import com.audiolaby.controller.request.SignInRequest;
import com.audiolaby.controller.request.SignInSocialRequest;
import com.audiolaby.controller.request.SignUpRequest;
import com.audiolaby.controller.request.SignUpSocialRequest;
import com.audiolaby.controller.request.VerifRequest;
import com.audiolaby.controller.response.AccountResponse;
import com.audiolaby.controller.response.CategoryListResponse;
import com.audiolaby.controller.response.CommonResponse;
import com.audiolaby.controller.response.SaveOrderResponse;
import com.audiolaby.controller.response.VerifResponse;
import com.audiolaby.controller.service.AccountService;
import com.audiolaby.persistence.AppPref_;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@EBean(scope = Scope.Singleton)
public class AccountController {
    public static final String BASE_URL;
    private AccountService accountService;
    @Bean
    AddCookiesInterceptor addCookiesInterceptor;
    @App
    Audiolaby audiolaby;
    private OkHttpClient okHttpClient;
    @Bean
    ReceivedCookiesInterceptor receivedCookiesInterceptor;
    private Retrofit retrofit;

    @Pref
    AppPref_ appPref;

    Context mContext;

    static {
        BASE_URL = ServerAddresses.BASE_REST.getValue();
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @AfterInject
    public void afterInjection() {

        this.okHttpClient = new Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(this.addCookiesInterceptor)
                .addInterceptor(this.receivedCookiesInterceptor).
               // sslSocketFactory(getSSLSocketFactory(mContext)).
                build();

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
        this.accountService = (AccountService) this.retrofit.create(AccountService.class);
    }


    public AccountResponse getUserDetails() {
        AccountResponse body = null;
        try {
            body = this.accountService.getUserDetails().execute().body();;

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new AccountResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }



    public VerifResponse verif(VerifRequest verifRequest) {
        VerifResponse body = null;
        try {
            body = this.accountService.verifUser(verifRequest).execute().body();;

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new VerifResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }



    public AccountResponse signIn(SignInRequest signInRequest) {
        AccountResponse body = null;
        try {
            body = this.accountService.signIn(signInRequest).execute().body();;

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new AccountResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }


    public AccountResponse signInSocial(SignInSocialRequest signInSocialRequest) {
        AccountResponse body = null;
        try {
            body = this.accountService.signInSocial(signInSocialRequest).execute().body();;

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new AccountResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }
        if (body != null) body.setThread(false);
        return body;
    }


    public AccountResponse signUp(SignUpRequest signUpRequest, File file) {
        AccountResponse body = null;
        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part multipartBody =MultipartBody.Part.createFormData("image",file.getName(),requestFile);
            body = this.accountService.signUp(
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getLoginType()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getEmail()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getPassword()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getFirstName()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getLastName()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getCountry()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getFcm_id()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getDevice_os()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getDevice_version()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getApp_version()),
                    RequestBody.create(MediaType.parse("text/plain"),""),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getGender()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getBirthday()),
                    multipartBody).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new AccountResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }

        if (body != null) body.setThread(false);
        return body;
    }


    public AccountResponse signUp(SignUpRequest signUpRequest) {
        AccountResponse body = null;
        try {
            body = this.accountService.signUp(
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getLoginType()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getEmail()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getPassword()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getFirstName()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getLastName()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getCountry()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getFcm_id()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getDevice_os()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getDevice_version()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getApp_version()),
                    RequestBody.create(MediaType.parse("text/plain"),""),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getGender()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpRequest.getBirthday())).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new AccountResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }

        if (body != null) body.setThread(false);
        return body;
    }

    public AccountResponse signUpSocial(SignUpSocialRequest signUpSocialRequest, File file) {
        AccountResponse body = null;
        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part multipartBody =MultipartBody.Part.createFormData("image",file.getName(),requestFile);
            body = this.accountService.signUpSocial(
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getLoginType()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getEmail()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getSocialId()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getFirstName()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getLastName()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getCountry()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getFcm_id()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getDevice_os()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getDevice_version()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getApp_version()),
                    RequestBody.create(MediaType.parse("text/plain"),""),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getGender()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getBirthday()),
                    multipartBody).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new AccountResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }

        if (body != null) body.setThread(false);
        return body;
    }


    public AccountResponse signUpSocial(SignUpSocialRequest signUpSocialRequest) {
        AccountResponse body = null;
        try {
            body = this.accountService.signUpSocial(
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getLoginType()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getEmail()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getSocialId()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getFirstName()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getLastName()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getCountry()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getFcm_id()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getDevice_os()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getDevice_version()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getApp_version()),
                    RequestBody.create(MediaType.parse("text/plain"),""),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getGender()),
                    RequestBody.create(MediaType.parse("text/plain"),signUpSocialRequest.getBirthday())
            ).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new AccountResponse();
                body.setThread(true);
                return body;
            }
            Log.i("audilaby-errors", e.toString());
        }

        if (body != null) body.setThread(false);
        return body;
    }


    public CommonResponse sendCode(ForgetPasswordRequest forgetPasswordRequest) {
        CommonResponse body = null;
        try {
            body= this.accountService.sendCode(forgetPasswordRequest).execute().body();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (body != null) body.setThread(false);
        return body;
    }


    public CommonResponse confirmCode(ConfirmCodeRequest confirmCodeRequest) {
        CommonResponse body = null;
        try {
            Response<CommonResponse> response = this.accountService.confirmCode(confirmCodeRequest).execute();
            body = (CommonResponse) response.body();
            if (response.errorBody() != null) {
                Log.i("eStories-errors", response.errorBody().string());
            }
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



    public CommonResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        CommonResponse body = null;
        try {
            body = this.accountService.resetPassword(resetPasswordRequest).execute().body();

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



    public SaveOrderResponse savePaidAudio(SavePaidAudioRequest savePaidAudioRequest) {
        SaveOrderResponse body = null;
        try {
            body = this.accountService.savePaidAudio(savePaidAudioRequest).execute().body();

        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                body = new SaveOrderResponse();
                body.setThread(true);
                return body;
            }
        }
        if (body != null) body.setThread(false);
        return body;
    }

    private void removeSessionCookies()
    {
        //appPref.token().put("");
    }


}
