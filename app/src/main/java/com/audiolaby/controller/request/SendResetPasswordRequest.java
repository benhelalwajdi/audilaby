package com.audiolaby.controller.request;

public class SendResetPasswordRequest {
    private String email;

    public SendResetPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
