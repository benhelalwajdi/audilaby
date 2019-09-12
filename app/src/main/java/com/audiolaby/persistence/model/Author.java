package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@Table(name = "author")
@EBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author extends Model implements Serializable {
    @Column(name = "author_id")
    private String author_id;
    @Column(name = "name")
    private String name;
    @Column(name = "size")
    @JsonProperty("nPost")
    private int size;
    @Column(name = "picture")
    @SerializedName("image")
    private String picture;


    @Column(name = "audioArticle", onDelete = Column.ForeignKeyAction.CASCADE, onUpdate = Column.ForeignKeyAction.CASCADE)
    public AudioArticle audioArticle;

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


}
