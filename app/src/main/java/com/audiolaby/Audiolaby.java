package com.audiolaby;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.util.ReflectionUtils;
import com.audiolaby.persistence.AppPref_;
import com.twitter.sdk.android.core.Twitter;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


@EApplication
public class Audiolaby extends MultiDexApplication {


    public  static String selected="";

    @Pref
    AppPref_ appPref;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCreate() {
        super.onCreate();
        Twitter.initialize(this);

        ActiveAndroid.initialize((Context) this);
        ActiveAndroid.setLoggingEnabled(true);


          /* if (appPref.update().get()) {
               }
            else{
               deleteDb();
               appPref.update().put(true);
           }*/


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("HacenMaghrebBd.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }


    public void deleteDb() {
//        ActiveAndroid.dispose();

        String aaName = ReflectionUtils.getMetaData(getApplicationContext(), "AA_DB_NAME");

        if (aaName == null) {
            aaName = "audiolaby.db";
        }

        deleteDatabase(aaName);
        ActiveAndroid.initialize(this);
        ActiveAndroid.setLoggingEnabled(true);

    }
}
