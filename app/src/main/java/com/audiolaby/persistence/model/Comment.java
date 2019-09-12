package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@EBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment  implements Serializable {
    private String comment_id;
    private String comment;
    @SerializedName("created_at")
    private Long date;
    private User user;
    private Boolean sending=false;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }



    public void setUser(User user) {
        this.user = user;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Boolean getSending() {
        return sending;
    }

    public void setSending(Boolean sending) {
        this.sending = sending;
    }
}



