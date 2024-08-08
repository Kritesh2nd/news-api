package com.exm.news.customRepo.app;

import com.exm.news.customRepo.app.interfaces.ArticleRepo;
import com.exm.news.dto.article.*;
import com.exm.news.entity.app.Article;
import com.exm.news.entity.app.Category;
import com.exm.news.entity.app.User;
import com.exm.news.entity.auth.Login;
import com.exm.news.model.ArticleRequest;
import com.exm.news.repository.app.CategoryRepository;
import com.exm.news.repository.app.UserRepository;
import com.exm.news.repository.auth.LoginRepository;
import com.exm.news.security.authentication.UserAuth;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;

@Transactional("appTransactionManager")
@Repository
public class ArticleRepoImplement implements ArticleRepo {

    @Autowired
    private ModelMapper modelMapper;

    @PersistenceContext(unitName = "appEntityManager")
    private EntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public List<FlexArticleDto> getArticleFlex(ArticleRequest requestType) {

        return List.of();
    }

    @Override
    public List<FlexArticleDto> getArticleFlexFull(ArticleRequest requestType) {
        String sql = "SELECT a.article_id, a.title, u.first_name, u.last_name FROM article a INNER JOIN users u ON a.author_id = u.id "+filterSqlQuery(requestType);
        Query query = manageQuery(sql,requestType,"FlexArticleDto");

        //noinspection unchecked
        return query.getResultList();
    }

    @Override
    public Integer getArticleCount() {
        String sql = "SELECT COUNT(*) AS total_count FROM article";
        Query query = entityManager.createNativeQuery(sql);

        // Cast the result to Number and then get the Integer value
        Number count = (Number) query.getSingleResult();

        // Return the count as an Integer
        return count.intValue();
    }

    @Override
    public List<MiniArticleDto> getSearchResultTitle(String keyWord){
        String sql = "SELECT a.article_id, a.title, u.first_name, u.last_name, a.short_content, a.publication_date, a.image_url FROM article a INNER JOIN users u ON a.author_id = u.id ";
        StringBuilder where = new StringBuilder();
        String[] keyWords = keyWord.split(" ");

        if (keyWords.length == 1) {
            where = new StringBuilder("WHERE a.title LIKE :keyWord0 ");
        }
        if (keyWords.length > 1) {
            for(int i=1; i<keyWords.length; i++){
                System.out.println("i: "+i);
                where.append("OR a.title LIKE :keyWord").append(i).append(" ");
            } 
        }
/*
        SELECT a.article_id, a.title, u.first_name, u.last_name FROM article a INNER JOIN users u ON a.author_id = u.id WHERE a.title LIKE '%A%';
        SELECT a.article_id, a.title, u.first_name, u.last_name FROM article a INNER JOIN users u ON a.author_id = u.id WHERE a.title LIKE '%A%' OFFSET 0 LIMIT 5;
*/
        sql += where;
        Query query = entityManager.createNativeQuery(sql, MiniArticleDto.class);
        for(int i=0; i<keyWords.length; i++){
            query.setParameter("keyWord"+i, "%"+keyWords[i]+"%");
        }
        query.setFirstResult(0);
        query.setMaxResults(5);

        //noinspection unchecked
        return query.getResultList();
    }

    @Override
    public List<TitleArticleDto> getTitleArticleList(ArticleRequest requestType){
        String sql = "SELECT a.article_id, a.title, u.first_name, " +
                "u.last_name FROM article a INNER JOIN users u ON " +
                "a.author_id = u.id "+filterSqlQuery(requestType);
        Query query = manageQuery(sql,requestType,"TitleArticleDto");
        //noinspection unchecked
        return query.getResultList();
    }

    @Override
    public List<TitleArticleDto> getTitleArticleImageList(ArticleRequest requestType) {
        String sql = "SELECT a.article_id, a.title, u.first_name, u.last_name, a.image_url  FROM article a INNER JOIN users u ON a.author_id = u.id "+filterSqlQuery(requestType);
        Query query = manageQuery(sql,requestType,"TitleArticleDto");

        //noinspection unchecked
        return query.getResultList();
    }

    @Override
    public List<MiniArticleDto> getMiniArticleList(ArticleRequest requestType) {
        String sql = "SELECT a.article_id, a.title, u.first_name, u.last_name, a.short_content, a.publication_date FROM article a INNER JOIN users u ON a.author_id = u.id "+filterSqlQuery(requestType);
        System.out.println("SQL Query: "+sql);
        Query query = manageQuery(sql,requestType,"MiniArticleDto");

        //noinspection unchecked
        return query.getResultList();
    }

    @Override
    public List<MiniArticleDto> getMiniArticleImageList(ArticleRequest requestType) {
//        String sql = "SELECT a.article_id, a.title, u.first_name, u.last_name, a.short_content, a.publication_date, a.image_url FROM article a INNER JOIN users u ON a.author_id = u.id "+filterSqlQuery(requestType);
//        String sql = "SELECT a.article_id, a.title, u.first_name, u.last_name, a.short_content, a.publication_date, a.image_url FROM article a INNER JOIN users u ON a.author_id = u.id LIMIT 0,5";
        String sql = "SELECT a.article_id, a.title, u.first_name, u.last_name, a.short_content, a.publication_date, a.image_url FROM article a INNER JOIN users u ON a.author_id = u.id "+filterSqlQuery(requestType);
        System.out.println("SQL Query Image: "+sql);
        Query query = manageQuery(sql,requestType,"MiniArticleDto");
//        Query query = entityManager.createNativeQuery(sql, MiniArticleDto.class);

        //noinspection unchecked
        return query.getResultList();
    }

    @Override
    public MainArticleDto getMainArticleById(Long articleId){
        String sql = "SELECT a.article_id, a.title, u.first_name, u.last_name, a.short_content, a.content, a.publication_date, a.image_url, c.category_name FROM article a INNER JOIN users u ON a.author_id = u.id INNER JOIN category c ON a.category_id = c.category_id WHERE a.article_id = :articleId";
        Query query = entityManager.createNativeQuery(sql, MainArticleDto.class);
        query.setParameter("articleId", articleId);

        return (MainArticleDto) query.getSingleResult();
    }

    @Override
    public List<MainArticleDto> getMainArticleList(ArticleRequest requestType) {
        String sql = "SELECT a.article_id, a.title, u.first_name, u.last_name, a.short_content, a.content, a.publication_date, a.image_url, c.category_name FROM article a INNER JOIN users u ON a.author_id = u.id INNER JOIN category c ON a.category_id = c.category_id "+filterSqlQuery(requestType);
        Query query = manageQuery(sql,requestType,"MainArticleDto");

        //noinspection unchecked
        return query.getResultList();
    }

    public String filterSqlQuery(ArticleRequest requestType){
        if(requestType.getFirstDate()!=null){
            checkValidDate(requestType.getFirstDate());
        }
        if(requestType.getSecondDate()!=null){
            checkValidDate(requestType.getSecondDate());
        }
        String finalQuery = requestType.getFirstDate()!=null?"WHERE DATE(a.publication_date) = :startDate":"";
        if(requestType.getFirstDate()!=null && requestType.getSecondDate()!=null){
            finalQuery = "WHERE DATE(a.publication_date) BETWEEN :startDate AND :endDate";
        }
        if(!finalQuery.isEmpty() && requestType.getCategory()!=null){
            finalQuery = finalQuery + " AND a.category_id = :categoryId ";
        }
        if(finalQuery.isEmpty() && requestType.getCategory()!=null){
            finalQuery = finalQuery + "WHERE a.category_id = :categoryId ";
        }
        finalQuery = finalQuery +"ORDER BY a.article_id ";
        if(requestType.isPagination()){
            String setPagination = " LIMIT :pageNumber, :pageSize ";
            finalQuery += setPagination;
        }
        return finalQuery;
    }

    private Query manageQuery(String sql, ArticleRequest requestType, String dtoName){
        entityManager.createNativeQuery(sql, TitleArticleDto.class);
        
        Query query = switch (dtoName) {
            case "MainArticleDto" -> entityManager.createNativeQuery(sql, MainArticleDto.class);
            case "MiniArticleDto" -> entityManager.createNativeQuery(sql, MiniArticleDto.class);
            case "TitleArticleDto" -> entityManager.createNativeQuery(sql, TitleArticleDto.class);
            case "FlexArticleDto" -> entityManager.createNativeQuery(sql, FlexArticleDto.class);
            default -> entityManager.createNativeQuery(sql);
        };
        if(requestType.getFirstDate()!=null){
            query.setParameter("startDate", requestType.getFirstDate());
        }
        if(requestType.getSecondDate()!=null){
            query.setParameter("endDate", requestType.getSecondDate());
        }
        if(requestType.getCategory()!=null){
            Category category = categoryRepository.findByCategoryName(requestType.getCategory());
            query.setParameter("categoryId", category.getCategoryId());
        }
        if(requestType.isPagination()){
            query.setParameter("pageNumber", requestType.getPageNumber());
            query.setParameter("pageSize", requestType.getPageSize());
        }
        return query;
    }

    public void checkValidDate(String dateStr) {
        String dateFormat = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        try {
            LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeException("Error in date time format: "+e);
        }
    }

    public void updateArticle(UpdateArticle updateArticle){
        Article article = entityManager.find(Article.class, updateArticle.getArticleId());
        if(article==null){
            throw new NoSuchElementException("Article not found");
        }
        article = modelMapper.map(updateArticle,Article.class);

        System.out.println("updateArticle: "+updateArticle.toString());
        System.out.println("article: "+article.toString());

        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication();
        Category category = categoryRepository.findByCategoryName(updateArticle.getCategory());
        Login loginAuthor = loginRepository.findLoginByEmail(userAuth.getEmail());
        User articleAuthor = userRepository.findUserById(loginAuthor.getId());

        article.setCategory(category);
        article.setAuthor(articleAuthor);
        article.setPublicationDate(LocalDateTime.now());

        entityManager.merge(article);
        entityManager.flush();
    }

    public void updateArticleQuery(UpdateArticle updateArticle) {

        System.out.println("in remo impl, "+updateArticle.toString());
//        String sql = "UPDATE article SET title=:title, short_content=:shortContent, image_url=:imageUrl, content=:content, publication_date=:publicationDate, author_id=:authorId, category_id=:categoryId WHERE article_id=:articleId;";
        String sql = "UPDATE article SET title=:title, short_content=:shortContent, content=:content, publication_date=:publicationDate, author_id=:authorId, category_id=:categoryId WHERE article_id=:articleId;";
        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication();
        Category category = categoryRepository.findByCategoryName(updateArticle.getCategory());
        Login loginAuthor = loginRepository.findLoginByEmail(userAuth.getEmail());

        Query query = entityManager.createNativeQuery(sql, Article.class);
        query.setParameter("title", updateArticle.getTitle());
        query.setParameter("shortContent", updateArticle.getShortContent());
//        query.setParameter("imageUrl", updateArticle.getImageUrl());
        query.setParameter("content", updateArticle.getContent());
        query.setParameter("publicationDate", LocalDateTime.now());
        query.setParameter("authorId", loginAuthor.getId());
        query.setParameter("categoryId", category.getCategoryId());
        query.setParameter("articleId", updateArticle.getArticleId());

        int updated = query.executeUpdate();
        entityManager.flush();
        if(updated <= 0){
            throw new RuntimeException("Couldn't article");
        }
        return;
    }


}
