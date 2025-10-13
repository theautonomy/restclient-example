package com.example.restclientdemo.model;

/** Custom search query object that will be handled by SearchQueryArgumentResolver. */
public record SearchQuery(String searchTerm, Integer limit, String sortBy, Boolean ascending) {

    public SearchQuery {
        // Default values
        if (limit == null) {
            limit = 10;
        }
        if (ascending == null) {
            ascending = true;
        }
    }

    public static SearchQuery of(String searchTerm) {
        return new SearchQuery(searchTerm, null, null, null);
    }

    public static SearchQuery of(String searchTerm, Integer limit) {
        return new SearchQuery(searchTerm, limit, null, null);
    }

    public static SearchQuery of(String searchTerm, Integer limit, String sortBy) {
        return new SearchQuery(searchTerm, limit, sortBy, null);
    }
}
