package com.audiolaby.controller.model;


import com.audiolaby.controller.enumeration.SortField;
import com.audiolaby.controller.enumeration.TypeField;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.lapism.searchview.SearchView;

import commons.validator.routines.AbstractNumberValidator;

public class  Pagination {
    private Integer page;
    private Integer rows;
    private String id;
    private String sort;
    private String user_id;


    public Pagination(Integer page, Integer rows) {
        this.page = page;
        this.rows = rows;
    }

    public Pagination(Integer page, Integer rows, String sort, String id) {
        this.page = page;
        this.rows = rows;
        this.sort = sort;
        this.id = id;
    }

    public Pagination(Integer page, Integer rows, String sort) {
        this.page = page;
        this.rows = rows;
        this.sort = sort;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
