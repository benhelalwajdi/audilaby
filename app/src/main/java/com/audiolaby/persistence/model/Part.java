package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@Table(name = "part")
@EBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Part extends Model implements Serializable {
    @Column(name = "title")
    private String title;
    @Column(name = "audioUrl")
    private String audioUrl;
    @Column(name = "localAudioUrl")
    private String localAudioUrl;
    @Column(name = "duration")
    private int duration;
    @Column(name = "position")
    private int position;




    @Column(name = "audioArticle", onDelete = Column.ForeignKeyAction.CASCADE, onUpdate = Column.ForeignKeyAction.CASCADE)
    public AudioArticle audioArticle;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getLocalAudioUrl() {
        return localAudioUrl;
    }

    public void setLocalAudioUrl(String localAudioUrl) {
        this.localAudioUrl = localAudioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
