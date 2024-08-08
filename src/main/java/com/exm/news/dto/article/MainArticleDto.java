package com.exm.news.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MainArticleDto {

    private Long articleId;

    private String title;

    private String authorFirstName;

    private String authorLastName;

    private String shortContent;

    private String content;

    private Date publishedDate;

    private String imageUrl;

    private String categoryName;


//    public MainArticleDto(Long articleId, String title, String authorFirstName, String authorLastName, String shortContent, String content, Date publishedDate, String imageUrl, String categoryName) {
//        this.articleId = articleId;
//        this.title = title;
//        this.authorFirstName = authorFirstName;
//        this.authorLastName = authorLastName;
//        this.shortContent = shortContent;
//        this.content = content;
//        this.publishedDate = null;
//        this.imageUrl = imageUrl;
//        this.calculatedDate = setCalculatedDate(publishedDate);
//    }
//
//    public String setCalculatedDate(Date date){
//        LocalDateTime startDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        LocalDateTime currentDateTime = LocalDateTime.now();
//
//        Duration duration = Duration.between(startDateTime, currentDateTime);
//        return formatDuration(duration);
//    }
//    private String formatDuration(Duration duration) {
//        System.out.println("duration: "+duration.toString());
//        long hours = duration.toHours();
//        long minutes = duration.toMinutes() % 60;
////        long seconds = duration.getSeconds() % 60;
//
//        return String.format("%d hours, %d minutes", hours, minutes);
//    }
}
