package com.audiolaby.controller.request;


public class SaveCommentRequest {

    private String post_id;
    private String comment;

    public SaveCommentRequest(String post_id, String comment) {
        this.post_id = post_id;
        this.comment = comment;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
