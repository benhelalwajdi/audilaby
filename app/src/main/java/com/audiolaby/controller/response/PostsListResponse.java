package com.audiolaby.controller.response;


import com.audiolaby.persistence.model.AudioArticle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostsListResponse extends CommonResponse {
    private List<AudioArticle> posts;


    public List<AudioArticle> getPosts() {
        return posts;
    }

    public void setPosts(List<AudioArticle> posts) {
        this.posts = posts;
    }
}
