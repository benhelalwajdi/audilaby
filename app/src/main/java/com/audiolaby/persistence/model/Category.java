package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.audiolaby.controller.enumeration.CategoryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@Table(name = "category")
@EBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category extends Model implements Serializable {
    @Column(name = "category_id")
    private String category_id;
    @Column(name = "name")
    private String name;
    @Column(name = "size")
    @SerializedName("nPost")
    private int size;
    @Column(name = "picture")
    @SerializedName("image")
    private String picture;

   private CategoryType categoryType;


    @Column(name = "audioArticle", onDelete = Column.ForeignKeyAction.CASCADE, onUpdate = Column.ForeignKeyAction.CASCADE)
    public AudioArticle audioArticle;


    public Category() {
        super();
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
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

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public AudioArticle getAudioArticle() {
        return audioArticle;
    }

    public void setAudioArticle(AudioArticle audioArticle) {
        this.audioArticle = audioArticle;
    }
}
