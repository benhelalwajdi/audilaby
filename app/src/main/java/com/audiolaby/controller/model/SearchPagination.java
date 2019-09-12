package com.audiolaby.controller.model;


public class SearchPagination {
    private Integer page;
    private Integer rows;
    private String keyword;
    private String sort;
    private String user_id;


    public SearchPagination(String keyword) {
        this.keyword = keyword;
    }


    public SearchPagination(String keyword, String user_id) {
        this.keyword = keyword;
        this.user_id = user_id;
    }

    public SearchPagination(Integer page, Integer rows, String keyword, String sort) {
        this.page = page;
        this.rows = rows;
        this.keyword = keyword;
        this.sort = sort;
    }

    public SearchPagination(Integer page, Integer rows, String keyword, String sort, String user_id) {
        this.page = page;
        this.rows = rows;
        this.keyword = keyword;
        this.sort = sort;
        this.user_id = user_id;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
