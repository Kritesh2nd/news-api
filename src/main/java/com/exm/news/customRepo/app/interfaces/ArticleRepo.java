package com.exm.news.customRepo.app.interfaces;

import com.exm.news.dto.article.FlexArticleDto;
import com.exm.news.dto.article.MainArticleDto;
import com.exm.news.dto.article.MiniArticleDto;
import com.exm.news.dto.article.TitleArticleDto;
import com.exm.news.model.ArticleRequest;

import java.util.List;

public interface ArticleRepo {

    public List<FlexArticleDto> getArticleFlex(ArticleRequest requestType);
    public List<FlexArticleDto> getArticleFlexFull(ArticleRequest requestType);
    public Integer getArticleCount();

    public List<MiniArticleDto> getSearchResultTitle(String keyWord);
    public List<TitleArticleDto> getTitleArticleList(ArticleRequest requestType);
    public List<TitleArticleDto> getTitleArticleImageList(ArticleRequest requestType);

    public List<MiniArticleDto> getMiniArticleList(ArticleRequest requestType);
    public List<MiniArticleDto> getMiniArticleImageList(ArticleRequest requestType);

    public MainArticleDto getMainArticleById(Long articleId);
    public List<MainArticleDto> getMainArticleList(ArticleRequest requestType);
}


/*
*
* get all article list
* get all article list pagination
*
* get all article list between two dates
* get all article list of a category
* get all article list between two dates and a given category
*
* get all article list between two dates, pagination
* get all article list of a category, pagination
* get all article list between two dates and a given category, pagination
*
* get micro article
* get short article
*
*
* */


/*
*
* all
* this date
* between two date
* this date, category
* between two date, category
*
*
*
* id, list of titles
* id, list of title and image
* TitleArticleDto
*
* id, title, author name, shortContent, published date
* id, title, author name, shortContent, published date, image
* MiniArticleDto
*
*
* id, title, author name, shortContent, content, published date, image, category name
* MainArticleDto
*
* */

/*


SELECT COUNT(*) AS article_id FROM article WHERE category_id = 2 OR category_id = 3  OR category_id = 4;
SELECT article_id, title FROM article WHERE category_id = 2 OR category_id = 3  OR category_id = 4;
SELECT article_id, title FROM article WHERE category_id = 2 OR category_id = 3  OR category_id = 4 LIMIT 5 OFFSET 0;
SELECT article_id, title FROM article WHERE category_id = 2 OR category_id = 3  OR category_id = 4 LIMIT 5 OFFSET 5;


* */