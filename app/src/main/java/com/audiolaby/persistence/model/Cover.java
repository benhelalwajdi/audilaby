package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@Table(name = "cover")
@EBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cover extends Model implements Serializable {
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "description2")
    @SerializedName("description2")
    private String description2;
    @Column(name = "url")
    private String url;
    @Column(name = "image")
    private String image;
    @Column(name = "donwloads")
    private int donwloads;
    @Column(name = "views")
    private int views;
    @Column(name = "stat")
    private Boolean stat;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDonwloads() {
        return donwloads;
    }

    public void setDonwloads(int donwloads) {
        this.donwloads = donwloads;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Boolean getStat() {
        return stat;
    }

    public void setStat(Boolean stat) {
        this.stat = stat;
    }
}
