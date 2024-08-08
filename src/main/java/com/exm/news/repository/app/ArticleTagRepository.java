package com.exm.news.repository.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exm.news.entity.app.ArticleTag;
@Repository
public interface ArticleTagRepository  extends JpaRepository<ArticleTag, Long> {

}
