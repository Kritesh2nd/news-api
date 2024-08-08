package com.exm.news.service;

import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exm.news.entity.app.Category;
import com.exm.news.repository.app.CategoryRepository;
import com.exm.news.response.BasicResponseDto;
import com.exm.news.service.interfaces.CategoryServiceInterface;
@Service
public class CategoryService implements CategoryServiceInterface{
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Category> getCategoryList() {
		List<Category> categoryList = categoryRepository.findAll();
		
		return categoryList;
	}
	
	public Category getCategoryById(Long id) {
		Optional<Category> category = categoryRepository.findById(id);
		
		if(!category.isPresent()) {
			throw new NoSuchElementException("Category not found");
		}
		
		return category.get();
	}

	@Override
	public BasicResponseDto addCategory(String category) {
		Category categoryExist = categoryRepository.findByCategoryName(category);
		
		if(categoryExist != null) {
			return new BasicResponseDto("Category already exits.",false);
		}
		
		Category newCategory = new Category();
		
		newCategory.setCategoryName(category);
		
		categoryRepository.save(newCategory);
		
		return new BasicResponseDto("New category added successfully.",true);
	}

	@Override
	public BasicResponseDto addAllCategories(List<String> category) {
		for(String c : category) {
			addCategory(c);
		}
		return new BasicResponseDto("All categories added successfully.",true);
	}

	@Override
	public BasicResponseDto updateCategory(Category newCategoryData) {
		try {
			Category updatedCategory = getCategoryById((Long) newCategoryData.getCategoryId());
			updatedCategory.setCategoryName(newCategoryData.getCategoryName());
			
			categoryRepository.save(updatedCategory);
		}
		catch(Exception e) {
			throw new NoSuchElementException("Category not found for update");
		}
		return new BasicResponseDto("Category updated successfully.",true);
	}

	@Override
	public BasicResponseDto deleteCategory(Long id) {
		Category deleteCategory = getCategoryById(id);
		categoryRepository.delete(deleteCategory);
		
		return new BasicResponseDto("Category deleted successfully.",true);
	}

	@Override
	public BasicResponseDto deleteCategory(String category) {
//		Using native query findByCategoryName()
		Category deleteCategory = categoryRepository.findByCategoryName(category);
		
		if(deleteCategory == null) {
			//TODO use of custom exception
			throw new NoSuchElementException("Category not found");
		}
		
		categoryRepository.delete(deleteCategory);
		return new BasicResponseDto("Category deleted successfully.",true);
	}

	@Override
	public BasicResponseDto deleteAllCategories() {
		categoryRepository.deleteAll();
		return new BasicResponseDto("All categories deleted successfully.",true);
	}

}
