package com.exm.news.repository.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.exm.news.entity.app.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	@Query(nativeQuery = true, value = "SELECT * FROM category c where c.category_id = :id")
	Category findByCategoryId(Long id);
	
	@Query(nativeQuery = true, value = "SELECT * FROM category c where c.category_name = :categoryName")
	Category findByCategoryName(String categoryName);
}