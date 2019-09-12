package com.audiolaby.controller.response;


import com.audiolaby.persistence.model.AudioArticle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDetailsResponse extends CommonResponse {
    private AudioArticle post;



    public AudioArticle getPost() {
        return post;
    }

    public void setPost(AudioArticle post) {
        this.post = post;
    }
}
