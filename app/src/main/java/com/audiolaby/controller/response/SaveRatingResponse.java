package com.audiolaby.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveRatingResponse extends CommonResponse {

    float rate;
    int n_rate;


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
}
