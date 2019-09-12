package com.audiolaby.controller.request;


public class ResetPasswordRequest {

    private String email;
    private String reset_code;
    private String password;
    private String confirm_password;


    public ResetPasswordRequest(String email, String reset_code, String password, String confirm_password) {
        this.email = email;
        this.reset_code = reset_code;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReset_code() {
        return reset_code;
    }

    public void setReset_code(String reset_code) {
        this.reset_code = reset_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }
}
