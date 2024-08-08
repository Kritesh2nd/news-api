package com.exm.news.controller;

import java.io.IOException;
import java.util.List;

import com.exm.news.dto.article.*;
import com.exm.news.model.ArticleRequest;
import com.exm.news.service.ArticleServiceImplement;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.exm.news.constant.PathConstant;
import com.exm.news.response.BasicResponseDto;

import jakarta.validation.Valid;

@RequestMapping(PathConstant.ARTICLE)
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ArticleController {
	
	@Autowired
	private ArticleServiceImplement articleServiceImplement;

	@GetMapping(PathConstant.CAT)
	public ResponseEntity<String> cat(){
		return new ResponseEntity<String>("Cat Article",HttpStatus.OK);
	}

	@GetMapping(PathConstant.COUNT)
	public ResponseEntity<Integer> getArticleCount(){
		return new ResponseEntity<Integer>(articleServiceImplement.getArticleCount(),HttpStatus.OK);
	}


	@GetMapping(PathConstant.LIST_TITLE_REQUEST)
	public ResponseEntity<List<MiniArticleDto>> getSearchResultTitleImage(
			@RequestParam(value = "search", defaultValue="") String search
	){
		return new ResponseEntity<List<MiniArticleDto>>(articleServiceImplement.setSearchResult(search),HttpStatus.OK);
	}

	@GetMapping(PathConstant.LIST_TITLE)
	public ResponseEntity<List<TitleArticleDto>> getTitleArticleList(
			@RequestParam(value = "firstDate", defaultValue="") String firstDate,
			@RequestParam(value = "secondDate", defaultValue="") String secondDate,
			@RequestParam(value = "category", defaultValue="") String category,
			@RequestParam(value = "pagination", defaultValue="false") boolean pagination,
			@RequestParam(value = "pageNumber", defaultValue="0") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue="0") int pageSize
	){
		ArticleRequest requestType = new ArticleRequest(firstDate,secondDate,category,pagination,pageNumber,pageSize);
		return new ResponseEntity<List<TitleArticleDto>>(articleServiceImplement.getTitleArticleList(requestType),HttpStatus.OK);
	}

	@GetMapping(PathConstant.LIST_TITLE_IMAGE)
	public ResponseEntity<List<TitleArticleDto>> getTitleArticleImageList(
			@RequestParam(value = "firstDate", defaultValue="") String firstDate,
			@RequestParam(value = "secondDate", defaultValue="") String secondDate,
			@RequestParam(value = "category", defaultValue="") String category,
			@RequestParam(value = "pagination", defaultValue="false") boolean pagination,
			@RequestParam(value = "pageNumber", defaultValue="0") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue="0") int pageSize
	){
		ArticleRequest requestType = new ArticleRequest(firstDate,secondDate,category,pagination,pageNumber,pageSize);
		return new ResponseEntity<List<TitleArticleDto>>(articleServiceImplement.getTitleArticleImageList(requestType),HttpStatus.OK);
	}

	@GetMapping(PathConstant.LIST_MINI)
	public ResponseEntity<List<MiniArticleDto>> getMiniArticleList(
			@RequestParam(value = "firstDate", defaultValue="") String firstDate,
			@RequestParam(value = "secondDate", defaultValue="") String secondDate,
			@RequestParam(value = "category", defaultValue="") String category,
			@RequestParam(value = "pagination", defaultValue="false") boolean pagination,
			@RequestParam(value = "pageNumber", defaultValue="0") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue="0") int pageSize
	){
		ArticleRequest requestType = new ArticleRequest(firstDate,secondDate,category,pagination,pageNumber,pageSize);
		return new ResponseEntity<List<MiniArticleDto>>(articleServiceImplement.getMiniArticleList(requestType),HttpStatus.OK);
	}

	@GetMapping(PathConstant.LIST_MINI_IMAGE)
	public ResponseEntity<List<MiniArticleDto>> getMiniArticleImageList(
			@RequestParam(value = "firstDate", defaultValue="") String firstDate,
			@RequestParam(value = "secondDate", defaultValue="") String secondDate,
			@RequestParam(value = "category", defaultValue="") String category,
			@RequestParam(value = "pagination", defaultValue="false") boolean pagination,
			@RequestParam(value = "pageNumber", defaultValue="0") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue="0") int pageSize
	){
		ArticleRequest requestType = new ArticleRequest(firstDate,secondDate,category,pagination,pageNumber,pageSize);
		System.out.println("PathConstant.LIST_MINI_IMAGE: "+requestType.toString());
		return new ResponseEntity<List<MiniArticleDto>>(articleServiceImplement.getMiniArticleImageList(requestType),HttpStatus.OK);
	}

	@GetMapping(PathConstant.LIST_MAIN)
	public ResponseEntity<List<MainArticleDto>> getMainArticleList(
			@RequestParam(value = "firstDate", defaultValue="") String firstDate,
			@RequestParam(value = "secondDate", defaultValue="") String secondDate,
			@RequestParam(value = "category", defaultValue="") String category,
			@RequestParam(value = "pagination", defaultValue="false") boolean pagination,
			@RequestParam(value = "pageNumber", defaultValue="0") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue="0") int pageSize
	){
		ArticleRequest requestType = new ArticleRequest(firstDate,secondDate,category,pagination,pageNumber,pageSize);
		return new ResponseEntity<List<MainArticleDto>>(articleServiceImplement.getMainArticleList(requestType),HttpStatus.OK);
	}

	@GetMapping(PathConstant.GET_BY_ID)
	public ResponseEntity<MainArticleDto> getMainArticleById(@PathVariable Long id){
		return new ResponseEntity<MainArticleDto>(articleServiceImplement.getMainArticleById(id),HttpStatus.OK);
	}


	@GetMapping(PathConstant.IMAGE_BY_URL)
	public ResponseEntity<?> getArticleImageByUrl(@PathVariable Long imageId){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(articleServiceImplement.getArticleImageById(imageId),headers,HttpStatus.OK);
	}


	
	
	
	@PreAuthorize("hasAnyAuthority('admin','editor')")
	@PostMapping("addContent")
	public ResponseEntity<BasicResponseDto> writeArticles(@RequestBody @Valid WriteArticle article){
		System.out.println("in ctrl");
		return new ResponseEntity<BasicResponseDto>(articleServiceImplement.writeArticle(article),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('admin','editor','reader')")
	@PostMapping(PathConstant.ADD)
	public ResponseEntity<BasicResponseDto> writeArticlesImg(@RequestPart("form") @Valid WriteArticle article, @RequestPart("img") MultipartFile... imageFiles) throws IOException{
		return new ResponseEntity<BasicResponseDto>(articleServiceImplement.writeArtileWithImages(article,imageFiles),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('admin','editor')")
	@PostMapping(PathConstant.ADD_ALL)
	public ResponseEntity<BasicResponseDto> writeAllArticle(@RequestBody List<WriteArticle> articles){
		return new ResponseEntity<BasicResponseDto>(articleServiceImplement.writeAllArticle(articles),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('admin','editor')")
	@PostMapping(PathConstant.UPDATE)
	public ResponseEntity<BasicResponseDto> updateArticle(@RequestPart("form") @Valid UpdateArticle article, @RequestPart("img") MultipartFile... imageFiles) throws IOException{

//		return new ResponseEntity<BasicResponseDto>(articleServiceImplement.updateArticle(article),HttpStatus.OK);
		return new ResponseEntity<BasicResponseDto>(articleServiceImplement.editArticle(article,imageFiles),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('admin','editor')")
	@PostMapping(PathConstant.DELETE_BY_ID)
	public ResponseEntity<BasicResponseDto> deleteById(@PathVariable Long id){
		return new ResponseEntity<BasicResponseDto>(articleServiceImplement.deleteArticleById(id),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('admin','editor')")
	@PostMapping(PathConstant.DELETE_ALL)
	public ResponseEntity<BasicResponseDto> deleteAll(){
		return new ResponseEntity<BasicResponseDto>(articleServiceImplement.deleteAllArticles(),HttpStatus.OK);
	}
	
}

