package com.audiolaby.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@EBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag implements Serializable {
    private String tag_id;
    private String name;

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}



