package com.audiolaby.persistence;


import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;


@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface AppPref {

    @DefaultBoolean(true)
    boolean firstTime();

    @DefaultBoolean(true)
    boolean offline();

    @DefaultString("")
    String token();

    @DefaultBoolean(false)
    boolean update();

    @DefaultInt(10)
    int listningScore();

    @DefaultInt(3)
    int downloadScore();

    @DefaultBoolean(true)
    boolean allowNotification();


}