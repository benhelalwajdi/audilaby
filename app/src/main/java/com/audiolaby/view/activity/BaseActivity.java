package com.audiolaby.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import com.audiolaby.R;
import com.audiolaby.controller.enumeration.ResponseMap;
import com.audiolaby.controller.enumeration.ResponseStatus;
import com.audiolaby.controller.response.CommonResponse;
import com.audiolaby.persistence.AppPref_;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.User;
import com.audiolaby.util.ConnectivityUtils;
import com.flipboard.bottomsheet.BottomSheetLayout;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.api.BackgroundExecutor;
import org.greenrobot.eventbus.EventBus;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


@EActivity
public abstract class BaseActivity extends AppCompatActivity {


    @Bean
    LibraryDAO libraryDAO;

    @Pref
    AppPref_ appPref;

    User user ;

    @StringRes(R.string.error_connection_ops)
    String errorConnection;
    @StringRes(R.string.error_try_again)
    String tryAgain;
    @StringRes(R.string.error_empty_table)
    String emptyTable;


    @ViewById(R.id.bottom_sheet)
    BottomSheetLayout layout;
    @ViewById(R.id.progress)
    ProgressBar progress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        if (this.layout != null) {
            this.layout.dismissSheet();
        }

    }



    protected void onDestroy() {
        super.onDestroy();
        BackgroundExecutor.cancelAll("longtask", true);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @CallSuper
    void afterViewsInjection() {
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("HacenMaghrebBd.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
    }

    public interface RefreshInfo {
        void onRefresh();

    }

    public String responseHandler(CommonResponse response) {

        if (response != null ) {
            if (response.getThread())
                return "again";
            else {

                if(response.getResponseStatus() == ResponseStatus.SUCCESS )
                    return "handler";
                else
                    return ResponseMap.getError(response.getResponseText(), this);
            }
        }
        else {
            if (!ConnectivityUtils.isConnected(this))  return errorConnection;
            else return tryAgain;
        }

    }

    @UiThread
    void showError(String message) {
        if(progress!=null)
            progress.setVisibility(View.GONE);
        Snackbar.make(getWindow().getDecorView().getRootView(), (CharSequence) message, Snackbar.LENGTH_LONG).show();
    }


    @UiThread
    void showProgress() {
        if(progress!=null)
            this.progress.setVisibility(View.VISIBLE);
    }

    @UiThread
    void dimissProgress() {
        if(progress!=null)
            this.progress.setVisibility(View.GONE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
