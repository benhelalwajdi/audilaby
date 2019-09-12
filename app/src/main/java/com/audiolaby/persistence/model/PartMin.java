package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartMin implements Serializable {
    private String title;
    private String audioUrl;
    private String localAudioUrl;
    private int duration;
    private int position;


    public PartMin(Part part) {
        this.title = part.getTitle();
        this.audioUrl = part.getAudioUrl();
        this.localAudioUrl = part.getLocalAudioUrl();
        this.duration = part.getDuration();
        this.position = part.getPosition();

    }


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
