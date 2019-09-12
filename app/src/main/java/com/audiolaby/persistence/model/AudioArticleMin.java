package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioArticleMin  implements Serializable {

    private String post_id;
    private Boolean is_parted;
    private List<PartMin> postParts;
    private String title;
    private String cover;
    private String voiceOver;



    public AudioArticleMin(AudioArticle audioArticle) {
        this.post_id = audioArticle.getPost_id();
        this.title = audioArticle.getTitle();
        //this.voiceOver = audioArticle.getVoiceOver();
        this.cover = audioArticle.getCover();
        this.is_parted = audioArticle.getIs_parted();
        this.postParts = new ArrayList<PartMin>();
        for(Part part:audioArticle.getPostParts())
        {
            postParts.add(new PartMin(part));
        }
    }

    public AudioArticleMin(String post_id, Boolean is_parted, List<PartMin> postParts, String title, String cover, String voiceOver) {
        this.post_id = post_id;
        this.is_parted = is_parted;
        this.postParts = postParts;
        this.title = title;
        this.cover = cover;
        this.voiceOver = voiceOver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getVoiceOver() {
        return voiceOver;
    }

    public void setVoiceOver(String voiceOver) {
        this.voiceOver = voiceOver;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public Boolean getIs_parted() {
        return is_parted;
    }

    public void setIs_parted(Boolean is_parted) {
        this.is_parted = is_parted;
    }

    public List<PartMin> getPostParts() {
        return postParts;
    }

    public void setPostParts(List<PartMin> postParts) {
        this.postParts = postParts;
    }
}
