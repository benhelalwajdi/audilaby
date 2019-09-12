package com.audiolaby.controller.response;


import com.audiolaby.persistence.model.Category;
import com.audiolaby.persistence.model.Section;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionListResponse extends CommonResponse {
   private List<Section> sections;

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
