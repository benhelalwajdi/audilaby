package com.audiolaby.controller.response;

import com.audiolaby.controller.enumeration.ResponseStatus;
import com.audiolaby.controller.enumeration.ResponseText;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonResponse {
    private ResponseStatus responseStatus;
    private ResponseText responseText;
    private Boolean isThread;

    public ResponseStatus getResponseStatus() {
        return this.responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public ResponseText getResponseText() {
        return this.responseText;
    }

    public void setResponseText(ResponseText responseText) {
        this.responseText = responseText;
    }

    public Boolean getThread() {
        return isThread;
    }

    public void setThread(Boolean thread) {
        isThread = thread;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "responseStatus=" + responseStatus +
                ", responseText=" + responseText +
                '}';
    }
}
