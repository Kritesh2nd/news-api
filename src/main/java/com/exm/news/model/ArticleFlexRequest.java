package com.exm.news.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleFlexRequest {

    private String author;

    private String firstDate;

    private String secondDate;

    private String category;

    private boolean pagination;

    private int pageNumber;

    private int pageSize;
}
