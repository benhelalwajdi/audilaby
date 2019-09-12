package com.audiolaby.controller.response;


import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.persistence.model.Part;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveOrderResponse extends CommonResponse {
    private String audioUrl;
    private List<Part> postParts;


    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }


    public List<Part> getPostParts() {
        return postParts;
    }

    public void setPostParts(List<Part> postParts) {
        this.postParts = postParts;
    }
}

