package com.audiolaby.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.audiolaby.Audiolaby;
import com.audiolaby.R;
import com.audiolaby.controller.AccountController;
import com.audiolaby.controller.enumeration.RegisterType;
import com.audiolaby.persistence.AppPref_;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.util.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.activity_intro)
public class IntroActivity extends BaseActivity {
    @Bean
    AccountController accountController;

    @Bean
    LibraryDAO libraryDAO;

    @App
    Audiolaby audiolaby;

    @Pref
    AppPref_ appPref;


    @Click({R.id.signup})
    void signUp() {
        Utils.hideKeyboard(IntroActivity.this);
        SignUpActivity_.intent((Context) IntroActivity.this).intro(true).registerType(RegisterType.email).start();
        finish();
    }


    @Click({R.id.browse})
    void browse() {
        Utils.hideKeyboard(IntroActivity.this);
        MainActivity_.intent((Context) IntroActivity.this).start();
        finish();
    }


    @Click({R.id.signin})
    void signin() {
        Utils.hideKeyboard(IntroActivity.this);
        SignInActivity_.intent((Context) IntroActivity.this).intro(true).start();
        finish();
    }


    @AfterViews
    void afterViewsInjection() {
        super.afterViewsInjection();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
    }


}
