package com.audiolaby.controller.request;


import com.audiolaby.controller.model.Pagination;

public class CommentsRequest {

    private String post_id;
    private Pagination pagination;

    public CommentsRequest(String post_id) {
        this.post_id = post_id;
    }


    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
