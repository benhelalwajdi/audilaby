package com.audiolaby.controller.request;


public class SignInSocialRequest {

    private String socialId;
    private String loginType;
    private String device_os;
    private String device_version;
    private String app_version;
    private String fcm_id;

    public SignInSocialRequest(String socialId, String loginType, String device_os, String device_version, String app_version, String fcm_id) {
        this.socialId = socialId;
        this.loginType = loginType;
        this.device_os = device_os;
        this.device_version = device_version;
        this.app_version = app_version;
        this.fcm_id = fcm_id;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
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
