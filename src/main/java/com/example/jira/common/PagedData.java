package com.example.jira.common;

import java.util.*;

public class PagedData<T> {
    private List<T> data;
    private Integer limit;
    private Integer offset;

    public PagedData() {
    }

    public PagedData(List<T> data, Integer limit, Integer offset) {
        this();
        this.data = data;
        this.limit = limit;
        this.offset = offset;
    }

    public List<T> getData() {
        return data;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }
}
