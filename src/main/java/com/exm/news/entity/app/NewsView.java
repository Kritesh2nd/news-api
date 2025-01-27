package com.exm.news.entity.app;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "news_view")
public class NewsView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long articleId;

    private String side;

    private LocalDateTime schedule;

    public NewsView(Long articleId, String side) {
        this.articleId = articleId;
        this.side = side;
    }
}

/*

id newsid side, schedule


*/
