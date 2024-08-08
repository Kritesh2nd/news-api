package com.exm.news.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.exm.news.entity.app.ArticleImage;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long>{
	
	@Query(nativeQuery = true, value="SELECT * FROM images i where i.article_id = :id")
	List<ArticleImage> findImagesByArticleId(Long id);

	@Query(nativeQuery = true, value="SELECT * FROM images i where i.img_id = :id")
	List<ArticleImage> findImagesByImageId(Long id);
}
