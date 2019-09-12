package com.audiolaby.controller.response;


import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.persistence.model.VoiceOver;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionItemResponse extends CommonResponse {
    private List<VoiceOver> voiceOvers;
    @SerializedName("posts")
    private List<AudioArticle> posts;

    public List<VoiceOver> getVoiceOvers() {
        return voiceOvers;
    }

    public void setVoiceOvers(List<VoiceOver> voiceOvers) {
        this.voiceOvers = voiceOvers;
    }

    public List<AudioArticle> getAudioPosts() {
        return posts;
    }

    public void setAudioPosts(List<AudioArticle> audioArticles) {
        this.posts = audioArticles;
    }
}
