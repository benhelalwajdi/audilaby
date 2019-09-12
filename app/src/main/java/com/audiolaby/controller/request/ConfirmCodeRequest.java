package com.audiolaby.controller.request;


public class ConfirmCodeRequest {

    private String email;
    private String reset_code;



    public ConfirmCodeRequest(String email, String reset_code) {
        this.email = email;
        this.reset_code = reset_code;
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
}
