package com.audiolaby.controller.response;


import com.audiolaby.persistence.model.Category;
import com.audiolaby.persistence.model.Cover;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoversResponse extends CommonResponse {
   private List<Cover> covers;
   private  int n_views;
   private  int n_downloads;

    public List<Cover> getCovers() {
        return covers;
    }

    public void setCovers(List<Cover> covers) {
        this.covers = covers;
    }

    public int getN_views() {
        return n_views;
    }

    public void setN_views(int n_views) {
        this.n_views = n_views;
    }

    public int getN_downloads() {
        return n_downloads;
    }

    public void setN_downloads(int n_downloads) {
        this.n_downloads = n_downloads;
    }
}
