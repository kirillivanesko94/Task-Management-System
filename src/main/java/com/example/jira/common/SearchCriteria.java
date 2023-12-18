package com.example.jira.common;

import org.springframework.util.*;

import java.util.*;

public class SearchCriteria {

    public static final Integer DEF_LIMIT = 100;

    private final Map<SearchFilter, String> filters = new HashMap<>();
    private final Integer limit;
    private final Integer offset;

    public SearchCriteria(Integer limit, Integer offset) {
        if (limit != null && limit <= 0) {
            throw new InvalidSearchParamValue("limit", limit);
        }

        if (offset != null && offset <= 0) {
            throw new InvalidSearchParamValue("offset", limit);
        }

        if (limit == null) {
            limit = DEF_LIMIT;
        }

        if (offset == null) {
            offset = 0;
        }

        this.limit = limit;
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public SearchCriteria addFilter(SearchFilter filter, String value) {
        if (filter == null || ObjectUtils.isEmpty(value)) {
            return this;
        }

        this.filters.put(filter, value.toLowerCase());

        return this;
    }

    public String getFilterValue(SearchFilter filter) {
        return filters.get(filter);
    }
}
