package com.audiolaby.controller.request;


public class ForgetPasswordRequest {

    private String email;


    public ForgetPasswordRequest(String email) {
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
