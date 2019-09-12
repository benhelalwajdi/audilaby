package com.audiolaby.controller.request;


import com.audiolaby.controller.model.Pagination;

public class SearchStoreRequest {
    private Pagination audiobookPagination;
    private Pagination authorPagination;
    private Pagination narratorPagination;
    private Pagination publisherPagination;
    private String textToFind;

    public SearchStoreRequest(String textToFind, Pagination narratorPagination, Pagination publisherPagination, Pagination audiobookPagination, Pagination authorPagination) {
        this.textToFind = textToFind;
        this.narratorPagination = narratorPagination;
        this.publisherPagination = publisherPagination;
        this.audiobookPagination = audiobookPagination;
        this.authorPagination = authorPagination;
    }

    public String getTextToFind() {
        return this.textToFind;
    }

    public void setTextToFind(String textToFind) {
        this.textToFind = textToFind;
    }

    public Pagination getNarratorPagination() {
        return this.narratorPagination;
    }

    public void setNarratorPagination(Pagination narratorPagination) {
        this.narratorPagination = narratorPagination;
    }

    public Pagination getPublisherPagination() {
        return this.publisherPagination;
    }

    public void setPublisherPagination(Pagination publisherPagination) {
        this.publisherPagination = publisherPagination;
    }

    public Pagination getAudiobookPagination() {
        return this.audiobookPagination;
    }

    public void setAudiobookPagination(Pagination audiobookPagination) {
        this.audiobookPagination = audiobookPagination;
    }

    public Pagination getAuthorPagination() {
        return this.authorPagination;
    }

    public void setAuthorPagination(Pagination authorPagination) {
        this.authorPagination = authorPagination;
    }
}
