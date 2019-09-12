package com.audiolaby.controller.request;


public class PostDetailsRequest {

    private String post_id;

    public PostDetailsRequest(String post_id) {
        this.post_id = post_id;
    }


    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
