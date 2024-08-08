package com.exm.news.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateArticle {

    private Long articleId;

    private String title;

    private String shortContent;

    private String content;

    private LocalDateTime publishedDate;

    private String category;

}
