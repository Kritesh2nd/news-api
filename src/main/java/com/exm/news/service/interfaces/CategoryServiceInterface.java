package com.exm.news.service.interfaces;

import java.util.List;

import com.exm.news.entity.app.Category;
import com.exm.news.response.BasicResponseDto;

public interface CategoryServiceInterface {

//	C[R]UD	
	public List<Category> getCategoryList();

//	[C]RUD
	public BasicResponseDto addCategory(String category);
	public BasicResponseDto addAllCategories(List<String> category);

//	CR[U]D
	public BasicResponseDto updateCategory(Category category);
	
//	CRU[D]
	public BasicResponseDto deleteCategory(Long id);
	public BasicResponseDto deleteCategory(String category);
	public BasicResponseDto deleteAllCategories();
}
