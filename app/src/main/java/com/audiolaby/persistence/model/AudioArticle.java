package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "audioArticle")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioArticle extends Model implements Serializable {

    @SerializedName("posts_id")
    @Column(name = "post_id")
    private String post_id;


    @Column(name = "title")
    private String title;


    @SerializedName("coverUrl")
    @Column(name = "cover")
    private String cover;


    @Column(name = "runtime")
    private int runtime;
    @Column(name = "description")
    private String description;
    @Column(name = "audioUrl")
    private String audioUrl;
    @Column(name = "localAudioUrl")
    private String localAudioUrl;
    @Column(name = "login_type")
    private String login_type;
    @Column(name = "isFree")
    private Boolean isFree;
    @SerializedName("created_at")
    @Column(name = "date")
    private Long date;
    @Column(name = "rate")
    private float rate;
    @Column(name = "n_rate")
    private int n_rate;
    @Column(name = "authrate")
    private int authrate;
    @SerializedName("postWished")
    @Column(name = "wished")
    private Boolean wished;
    @Column(name = "views")
    private int views;
    @Column(name = "downloads")
    private int downloads;
    @Column(name = "library")
    private Boolean library;
    @Column(name = "thumbnail")
    private String thumbnail;


    @Column(name = "code")
    private String code;
    @Column(name = "amount")
    private float amount;
    @Column(name = "is_parted")
    private Boolean is_parted;
    @Column(name = "authPaid")
    private Boolean authPaid;


    private List<Part> postParts;


    private List<Tag> tags;
    private List<Comment> comments;


    private List<AudioArticle> relatedAuthorPosts;
    private List<AudioArticle> relatedCategoryPosts;
    private List<AudioArticle> relatedVoiceOversPosts;


    private  Category category;
    private Author author;
    private VoiceOver voiceOver;


    public List<Part> getPostPartsList()
    {
        return getMany(Part.class, "audioArticle");
    }


    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public VoiceOver getVoiceOver() {
        return voiceOver;
    }

    public void setVoiceOver(VoiceOver voiceOver) {
        this.voiceOver = voiceOver;
    }

    public Boolean getFree() {
        return isFree;
    }

    public void setFree(Boolean free) {
        isFree = free;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getN_rate() {
        return n_rate;
    }

    public void setN_rate(int n_rate) {
        this.n_rate = n_rate;
    }

    public Boolean getWished() {
        return wished;
    }

    public void setWished(Boolean wished) {
        this.wished = wished;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<AudioArticle> getRelatedAuthorPosts() {
        return relatedAuthorPosts;
    }

    public void setRelatedAuthorPosts(List<AudioArticle> relatedAuthorPosts) {
        this.relatedAuthorPosts = relatedAuthorPosts;
    }

    public List<AudioArticle> getRelatedCategoryPosts() {
        return relatedCategoryPosts;
    }

    public void setRelatedCategoryPosts(List<AudioArticle> relatedCategoryPosts) {
        this.relatedCategoryPosts = relatedCategoryPosts;
    }

    public List<AudioArticle> getRelatedVoiceOversPosts() {
        return relatedVoiceOversPosts;
    }

    public void setRelatedVoiceOversPosts(List<AudioArticle> relatedVoiceOversPosts) {
        this.relatedVoiceOversPosts = relatedVoiceOversPosts;
    }

    public String getLocalAudioUrl() {
        return localAudioUrl;
    }

    public void setLocalAudioUrl(String localAudioUrl) {
        this.localAudioUrl = localAudioUrl;
    }

    public int getAuthrate() {
        return authrate;
    }

    public void setAuthrate(int authrate) {
        this.authrate = authrate;
    }

    public Boolean getLibrary() {
        return library;
    }

    public void setLibrary(Boolean library) {
        this.library = library;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Boolean getIs_parted() {
        return is_parted;
    }

    public void setIs_parted(Boolean is_parted) {
        this.is_parted = is_parted;
    }

    public Boolean getAuthPaid() {
        return authPaid;
    }

    public void setAuthPaid(Boolean authPaid) {
        this.authPaid = authPaid;
    }


    public List<Part> getPostParts() {
        return postParts;
    }

    public void setPostParts(List<Part> postParts) {
        this.postParts = postParts;
    }

    public String getThumbnail() {
        if(thumbnail!=null)
        return thumbnail;
        else
            return cover;

    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
