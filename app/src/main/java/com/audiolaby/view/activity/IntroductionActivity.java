package com.audiolaby.view.activity;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.audiolaby.Audiolaby;
import com.audiolaby.R;
import com.audiolaby.persistence.AppPref_;
import com.audiolaby.view.fragment.IntroductionFragment;
import com.github.paolorotolo.appintro.AppIntro;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;


@EActivity
public class IntroductionActivity extends AppIntro {

    @Pref
    AppPref_ appPref;
    @App
    Audiolaby audiolaby;

    @StringRes(R.string.done)
    String done;
    @ColorRes(R.color.colorAccent)
    int selected;
    @ColorRes(R.color.white)
    int unselected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(IntroductionFragment.newInstance(R.layout.fragment_intro1));
        addSlide(IntroductionFragment.newInstance(R.layout.fragment_intro2));
        addSlide(IntroductionFragment.newInstance(R.layout.fragment_intro3));

        setBarColor(Color.TRANSPARENT);
        setSeparatorColor(Color.TRANSPARENT);

        showSkipButton(false);
        setProgressButtonEnabled(true);
        setDoneText(done);

        setVibrate(true);
        setVibrateIntensity(30);
        setDepthAnimation();
        setIndicatorColor(selected,Color.WHITE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        appPref.firstTime().put(false);
        MainActivity_.intent((Context) this).start();
        finish();
    }

}

