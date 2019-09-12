package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@Table(name = "sectionItem")
@EBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionItem extends Model implements Serializable {
    AudioArticle audioArticle;
    VoiceOver voiceOver;

    public AudioArticle getAudioArticle() {
        return audioArticle;
    }

    public void setAudioArticle(AudioArticle audioArticle) {
        this.audioArticle = audioArticle;
    }

    public VoiceOver getVoiceOver() {
        return voiceOver;
    }

    public void setVoiceOver(VoiceOver voiceOver) {
        this.voiceOver = voiceOver;
    }
}
