package com.exm.news.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TitleArticleDto {

    private Long articleId;

    private String title;

    private String authorFirstName;

    private String authorLastName;

    private String imageUrl;

    public TitleArticleDto(Long articleId, String title, String authorFirstName, String authorLastName) {
        this.articleId = articleId;
        this.title = title;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
    }
}
