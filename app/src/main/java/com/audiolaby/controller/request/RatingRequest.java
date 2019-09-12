package com.audiolaby.controller.request;


public class RatingRequest {

    private String post_id;
    private int  rate;

    public RatingRequest(String post_id, int rate) {
        this.post_id = post_id;
        this.rate = rate;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
