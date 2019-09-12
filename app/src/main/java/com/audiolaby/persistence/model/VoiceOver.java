package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@Table(name = "voice_over")
@EBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoiceOver extends Model implements Serializable {
    @Column(name = "voiceOver_id")
    private String voiceOver_id;
    @Column(name = "name")
    private String name;
    @Column(name = "size")
    @SerializedName("nPost")
    private int size;
    @Column(name = "picture")
    @SerializedName("image")
    private String picture;

    @Column(name = "audioArticle", onDelete = Column.ForeignKeyAction.CASCADE, onUpdate = Column.ForeignKeyAction.CASCADE)
    public AudioArticle audioArticle;


    public VoiceOver() {
        super();
    }

    public String getVoiceOver_id() {
        return voiceOver_id;
    }

    public void setVoiceOver_id(String voiceOver_id) {
        this.voiceOver_id = voiceOver_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public int getSize() {
//        return size;
//    }
//
//    public void setSize(int size) {
//        this.size = size;
//    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
