package com.exm.news.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exm.news.constant.PathConstant;
import com.exm.news.entity.app.Category;
import com.exm.news.response.BasicResponseDto;
import com.exm.news.service.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;

@RequestMapping(PathConstant.CATEGORY)
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping(PathConstant.CAT)
	public ResponseEntity<String> cat(){
		return new ResponseEntity<String>("Cat Category",HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAnyAuthority('admin','editor','reader')")
	@GetMapping(PathConstant.LIST)
	public ResponseEntity<List<Category>> categoryList(){
		return new ResponseEntity<List<Category>>(categoryService.getCategoryList(),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('admin','editor')")
	@GetMapping(PathConstant.GET_BY_ID)
	public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
		return new ResponseEntity<Category>(categoryService.getCategoryById(id),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('admin')")
	@PostMapping(PathConstant.ADD)
	public ResponseEntity<BasicResponseDto> addCategory(@RequestBody @NotBlank(message="input field cannot be blank") String category){
		return new ResponseEntity<BasicResponseDto>(categoryService.addCategory(category),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('admin')")
	@PostMapping(PathConstant.ADD_ALL)
	public ResponseEntity<BasicResponseDto> addCategories(@RequestBody List<String> category){
		return new ResponseEntity<BasicResponseDto>(categoryService.addAllCategories(category),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('admin')")
	@PostMapping(PathConstant.UPDATE)
	public ResponseEntity<BasicResponseDto> updateCategory(@RequestBody @Valid Category category){
		return new ResponseEntity<BasicResponseDto>(categoryService.updateCategory(category),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('admin')")
	@PostMapping(PathConstant.DELETE_ALL)
	public ResponseEntity<BasicResponseDto> deleteAll(){
		return new ResponseEntity<BasicResponseDto>(categoryService.deleteAllCategories(),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('admin','editor')")
	@PostMapping(PathConstant.DELETE_BY_ID)
	public ResponseEntity<BasicResponseDto> deleteById(@PathVariable Long id){
		return new ResponseEntity<BasicResponseDto>(categoryService.deleteCategory(id),HttpStatus.OK);
	}

}
