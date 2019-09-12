package com.audiolaby.controller.request;


import com.audiolaby.controller.model.Pagination;

public class SectionItemsRequest {

    private String section_key;
    private Pagination pagination;


    public SectionItemsRequest(String section_key) {
        this.section_key = section_key;
    }

    public SectionItemsRequest(String section_key, Pagination pagination) {
        this.section_key = section_key;
        this.pagination = pagination;
    }

    public String getSection_key() {
        return section_key;
    }

    public void setSection_key(String section_key) {
        this.section_key = section_key;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
