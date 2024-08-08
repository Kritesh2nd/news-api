package com.exm.news.service.interfaces;

import java.io.IOException;
import java.util.List;

import com.exm.news.dto.article.*;
import com.exm.news.model.ArticleRequest;
import org.springframework.web.multipart.MultipartFile;

import com.exm.news.response.BasicResponseDto;

public interface ArticleService {

//	C[R]UD
	public Integer getArticleCount();
	public List<MiniArticleDto> setSearchResult(String keyWord);
	public List<TitleArticleDto> getTitleArticleList(ArticleRequest requestType);
	public List<TitleArticleDto> getTitleArticleImageList(ArticleRequest requestType);
	public List<MiniArticleDto> getMiniArticleList(ArticleRequest requestType);
	public List<MiniArticleDto> getMiniArticleImageList(ArticleRequest requestType);
	public MainArticleDto getMainArticleById(Long articleId);
	public List<MainArticleDto> getMainArticleList(ArticleRequest requestType);
	public byte[] getArticleImageById(Long imageId);

	//	[C]RUD
	public BasicResponseDto writeArticle(WriteArticle newArticle);
	public BasicResponseDto writeAllArticle(List<WriteArticle> articles);
	public BasicResponseDto writeArtileWithImages(WriteArticle newArticle, MultipartFile... files) throws IOException;
	
//	CR[U]D
	public BasicResponseDto editArticle(UpdateArticle updateArticle, MultipartFile... files) throws IOException ;
	
//	CRU[D]
	public BasicResponseDto deleteArticleById(Long id);
	public BasicResponseDto deleteAllArticles();
}
