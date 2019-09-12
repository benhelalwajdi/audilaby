package com.audiolaby.controller.request;


import com.audiolaby.controller.model.Pagination;

public class AutocompleteStoreRequest extends SearchStoreRequest {
    public AutocompleteStoreRequest(String textToFind, Pagination narratorPagination, Pagination publisherPagination, Pagination audiobookPagination, Pagination authorPagination) {
        super(textToFind, narratorPagination, publisherPagination, audiobookPagination, authorPagination);
    }
}
