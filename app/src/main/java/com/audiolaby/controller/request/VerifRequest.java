package com.audiolaby.controller.request;


public class VerifRequest {

    private String app_id;
    private String loginType;

    public VerifRequest(String app_id, String loginType) {
        this.app_id = app_id;
        this.loginType = loginType;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
