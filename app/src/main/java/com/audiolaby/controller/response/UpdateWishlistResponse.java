package com.audiolaby.controller.response;


import android.app.Activity;

import com.audiolaby.controller.enumeration.ActionField;
import com.audiolaby.persistence.model.Cover;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateWishlistResponse extends CommonResponse {
   private ActionField action;

    public ActionField getAction() {
        return action;
    }

    public void setAction(ActionField action) {
        this.action = action;
    }
}
