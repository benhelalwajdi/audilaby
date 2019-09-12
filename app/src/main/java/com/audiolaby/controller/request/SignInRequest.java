package com.audiolaby.controller.request;


public class SignInRequest {

    private String email;
    private String password;
    private String loginType;
    private String device_os;
    private String device_version;
    private String app_version;
    private String fcm_id;


//    email
//            password
//    device_version
//            app_version
//    fcm_id
//            loginType
//    device_os


    public SignInRequest(String email, String password, String loginType, String device_os, String device_version, String app_version, String fcm_id) {
        this.email = email;
        this.password = password;
        this.device_version = device_version;
        this.loginType = loginType;
        this.device_os = device_os;
        this.app_version = app_version;
        this.fcm_id = fcm_id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getDevice_os() {
        return device_os;
    }

    public void setDevice_os(String device_os) {
        this.device_os = device_os;
    }

    public String getDevice_version() {
        return device_version;
    }

    public void setDevice_version(String device_version) {
        this.device_version = device_version;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }
}
