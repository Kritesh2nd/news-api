package com.exm.news.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlexArticleDto {

    private Long articleId;

    private String title;

    private String authorFirstName;

    private String authorLastName;

    private String shortContent;

    private String content;

    private String publishedDate;

    private String imageUrl;

    private String categoryName;

}
