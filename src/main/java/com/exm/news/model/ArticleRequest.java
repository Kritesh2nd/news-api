package com.exm.news.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ArticleRequest {

    private String firstDate;

    private String secondDate;

    private String category;

    private boolean pagination;

    private int pageNumber;

    private int pageSize;

    public ArticleRequest(String firstDate, String secondDate, String category, boolean pagination, int pageNumber, int pageSize) {
        this.firstDate = firstDate.isBlank() ? null : firstDate;
        this.secondDate = secondDate.isBlank() ? null : secondDate;
        this.category = category.isBlank() ? null : category;
        this.pagination = pagination;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

}
