package com.audiolaby.controller.request;


public class GetUserIdRequest {

    private String user_id;


    public GetUserIdRequest(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
