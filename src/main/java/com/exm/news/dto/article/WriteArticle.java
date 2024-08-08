package com.exm.news.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WriteArticle {

    private Long articleId;

    @NotBlank(message = "input field cannot be blank")
    private String title;

    @NotBlank(message = "input field cannot be blank")
    private String shortContent;

    private String imageUrl;

    @NotBlank(message = "input field cannot be blank")
    private String content;

    @NotBlank(message = "input field cannot be blank")
    private String category;
}
