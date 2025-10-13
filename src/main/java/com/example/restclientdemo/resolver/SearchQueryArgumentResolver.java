package com.example.restclientdemo.resolver;

import com.example.restclientdemo.model.SearchQuery;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;

/** Custom argument resolver for SearchQuery objects in HTTP interfaces. */
public class SearchQueryArgumentResolver implements HttpServiceArgumentResolver {

    @Override
    public boolean resolve(
            @Nullable Object argument,
            MethodParameter parameter,
            HttpRequestValues.Builder requestValues) {

        // Check if this resolver can handle the parameter type
        if (!parameter.getParameterType().equals(SearchQuery.class)) {
            return false;
        }

        // Handle null arguments gracefully
        if (argument == null) {
            return true;
        }

        SearchQuery searchQuery = (SearchQuery) argument;

        // Add search term as a query parameter
        if (searchQuery.searchTerm() != null && !searchQuery.searchTerm().isBlank()) {
            requestValues.addRequestParameter("q", searchQuery.searchTerm());
        }

        // Add limit parameter
        if (searchQuery.limit() != null) {
            requestValues.addRequestParameter("limit", String.valueOf(searchQuery.limit()));
        }

        // Add sort parameter
        if (searchQuery.sortBy() != null && !searchQuery.sortBy().isBlank()) {
            requestValues.addRequestParameter("sort", searchQuery.sortBy());
        }

        // Add order parameter based on ascending flag
        if (searchQuery.ascending() != null) {
            requestValues.addRequestParameter("order", searchQuery.ascending() ? "asc" : "desc");
        }

        // Return true to indicate this resolver handled the argument
        return true;
    }
}
