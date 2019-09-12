package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@Table(name = "wish")
@EBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class WishListItem extends Model implements Serializable {
    @Column(name = "wish_id")
    private String wish_id;
    @Column(name = "title")
    private String title;
    @Column(name = "coverUrl")
    private String cover;
    @Column(name = "runtime")
    private String runtime;
    @Column(name = "description")
    private String description;
    @Column(name = "audioUrl")
    private String audioUrl;
    @Column(name = "login_type")
    private String login_type;

    private Author author;
    private VoiceOver voiceOver;


    public String getWish_id() {
        return wish_id;
    }

    public void setWish_id(String wish_id) {
        this.wish_id = wish_id;
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

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public VoiceOver getVoiceOver() {
        return voiceOver;
    }

    public void setVoiceOver(VoiceOver voiceOver) {
        this.voiceOver = voiceOver;
    }
}
