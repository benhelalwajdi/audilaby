package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.audiolaby.controller.enumeration.SectionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@Table(name = "section")
@EBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class Section extends Model implements Serializable {
    @Column(name = "section_id")
    private String section_id;
    @Column(name = "name")
    private String name;
    @Column(name = "position")
    private int position;
    @Column(name = "active")
    private Boolean active;
    @SerializedName("section_key")
    private SectionType sectionType;

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SectionType getSectionType() {
        return sectionType;
    }

    public void setSectionType(SectionType sectionType) {
        this.sectionType = sectionType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
