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
public class MiniArticleDto {

    private Long articleId;

    private String title;

    private String authorFirstName;

    private String authorLastName;

    private String shortContent;

    private Date publishedDate;

    private String imageUrl;

    public MiniArticleDto(Long articleId, String title, String authorFirstName, String authorLastName, String shortContent, Date publishedDate) {
        this.articleId = articleId;
        this.title = title;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.shortContent = shortContent;
        this.publishedDate = publishedDate;
    }
}
