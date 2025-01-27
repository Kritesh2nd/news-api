package com.exm.news.repository.app;

import com.exm.news.entity.app.NewsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsViewRepository extends JpaRepository<NewsView, Long> {

    List<NewsView> findBySide(String side);

    Optional<NewsView> findByArticleIdAndSide(Long articleId, String side);
}
