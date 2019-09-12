package com.audiolaby.controller.response;


import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.persistence.model.Author;
import com.audiolaby.persistence.model.VoiceOver;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse extends CommonResponse {
    private List<AudioArticle> posts;
    private List<VoiceOver> voiceOvers;
    private List<Author> authors;


    public List<AudioArticle> getPosts() {
        return posts;
    }

    public void setPosts(List<AudioArticle> posts) {
        this.posts = posts;
    }

    public List<VoiceOver> getVoiceOvers() {
        return voiceOvers;
    }

    public void setVoiceOvers(List<VoiceOver> voiceOvers) {
        this.voiceOvers = voiceOvers;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
