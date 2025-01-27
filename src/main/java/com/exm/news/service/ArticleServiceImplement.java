package com.exm.news.service;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.exm.news.customRepo.app.ArticleRepoImplement;
import com.exm.news.dto.article.*;
import com.exm.news.entity.app.*;
import com.exm.news.entity.auth.Login;
import com.exm.news.model.ArticleRequest;
import com.exm.news.repository.auth.LoginRepository;
import com.exm.news.service.interfaces.ArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exm.news.dto.user.GeneralUserDto;
import com.exm.news.repository.app.ArticleImageRepository;
import com.exm.news.repository.app.ArticleRepository;
import com.exm.news.repository.app.CategoryRepository;
import com.exm.news.repository.app.UserRepository;
import com.exm.news.response.BasicResponseDto;
import com.exm.news.security.authentication.UserAuth;


@Service
public class ArticleServiceImplement implements ArticleService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleRepoImplement articleRepoImplement;

    @Autowired
    private ArticleImageRepository articleImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private NewsViewServiceImpl newsViewService;

    @Override
    public Integer getArticleCount() {
        return articleRepoImplement.getArticleCount();
    }

    @Override
    public List<MiniArticleDto> setSearchResult(String keyWord) {
        return articleRepoImplement.getSearchResultTitle(keyWord);
    }

    @Override
    public List<TitleArticleDto> getTitleArticleList(ArticleRequest requestType) {
        return articleRepoImplement.getTitleArticleList(requestType);
    }

    @Override
    public List<TitleArticleDto> getTitleArticleImageList(ArticleRequest requestType) {
        return articleRepoImplement.getTitleArticleImageList(requestType);
    }

    @Override
    public List<MiniArticleDto> getMiniArticleList(ArticleRequest requestType) {
        return articleRepoImplement.getMiniArticleList(requestType);
    }

    @Override
    public List<MiniArticleDto> getMiniArticleImageList(ArticleRequest requestType) {
        return articleRepoImplement.getMiniArticleImageList(requestType);
    }

    @Override
    public MainArticleDto getMainArticleById(Long articleId) {
        return articleRepoImplement.getMainArticleById(articleId);
    }

    @Override
    public List<MainArticleDto> getMainArticleList(ArticleRequest requestType) {
        return articleRepoImplement.getMainArticleList(requestType);
    }

    @Override
    public List<MainArticleDto> listArticleBySide(String side) {
        List<NewsView> newsViewList = newsViewService.listNewsViewBySide(side);
        if (newsViewList.isEmpty()) return List.of();

        List<MainArticleDto> mainArticleDtoList = newsViewList.stream().map(
                newsView -> {
                    Article article = articleRepository.findArticleById(newsView.getId());
                    MainArticleDto mainArticleDto = modelMapper.map(article, MainArticleDto.class);
                    mainArticleDto.setAuthorFirstName(article.getAuthor().getFirstName());
                    mainArticleDto.setAuthorLastName(article.getAuthor().getLastName());
                    Date publishedDate = Date.from(article.getPublicationDate().atZone(ZoneId.systemDefault()).toInstant());
                    mainArticleDto.setPublishedDate(publishedDate);
                    mainArticleDto.setCategoryName(article.getCategory().getCategoryName());
                    return mainArticleDto;
                }
        ).toList();


        return mainArticleDtoList;
    }

    @Override
    public byte[] getArticleImageById(Long imageId) {
        //http://localhost:8080/article/image/1

        if (imageId == null || imageId < 1) {
            throw new NoSuchElementException("Image not found");
        }
        List<ArticleImage> articleImages = articleImageRepository.findImagesByImageId(imageId);

        if (articleImages.isEmpty()) {
            throw new NoSuchElementException("Image not found");
        }

        return java.util.Base64.getDecoder().decode(articleImages.get(0).getImage());
    }

    @Override
    public BasicResponseDto writeArticle(WriteArticle newArticle) {
        Article article = modelMapper.map(newArticle, Article.class);

        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication();

        Login loginAuthor = loginRepository.findLoginByEmail(userAuth.getEmail());

        User articleAuther = userRepository.findUserById(loginAuthor.getId());

        Category articleCategory = categoryRepository.findByCategoryName(newArticle.getCategory());

        if (articleCategory == null) {
            throw new NoSuchElementException("Category not found: " + newArticle.getCategory());
        }

        Article lastArticle = articleRepository.findLastArticle();
        int imageId = lastArticle == null ? 1 : (int) (lastArticle.getArticleId() + 1);

        article.setAuthor(articleAuther);
        article.setCategory(articleCategory);
        article.setPublicationDate(LocalDateTime.now());

        article.setImageUrl("http://localhost:8080/article/image/" + imageId);

        articleRepository.save(article);

        return new BasicResponseDto("New article added successfully", true);
    }

    @Override
    public BasicResponseDto writeAllArticle(List<WriteArticle> articles) {
        for (WriteArticle a : articles) {
            writeArticle(a);
        }
        return new BasicResponseDto("All articles added successfully.", true);
    }

    @Override
    public BasicResponseDto writeArtileWithImages(WriteArticle newArticle, MultipartFile... files) throws IOException {
        writeArticle(newArticle);
        Article LastArticle = articleRepository.findLastArticle();

        List<ArticleImage> articleImages = new ArrayList<ArticleImage>();

        for (MultipartFile file : files) {
            System.out.println("file type: " + file.getContentType());
            String imgFile = Base64.getEncoder().encodeToString(file.getBytes());
            ArticleImage articleImage = new ArticleImage(Long.valueOf(0), imgFile, LastArticle);
            articleImages.add(articleImage);
        }

        articleImageRepository.saveAll(articleImages);
        return new BasicResponseDto("New article added with image successfully", true);
    }

    @Override
    public BasicResponseDto editArticle(UpdateArticle updateArticle, MultipartFile... files) throws IOException {
        Article findArticle = getArticleById(updateArticle.getArticleId());
        if (findArticle == null) {
            throw new NoSuchElementException("Article not found");
        }
        Article updateArticleData = modelMapper.map(updateArticle, Article.class);
        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication();
        Login loginAuthor = loginRepository.findLoginByEmail(userAuth.getEmail());
        User articleAuthor = userRepository.findUserById(loginAuthor.getId());
        String categoryName = updateArticle.getCategory();

        Category articleCategory = categoryRepository.findByCategoryName(categoryName);
        if (articleCategory == null) {
            throw new NoSuchElementException("Category not found");
        }
        updateArticleData.setAuthor(articleAuthor);
        updateArticleData.setCategory(articleCategory);
        updateArticleData.setPublicationDate(LocalDateTime.now());
        updateArticleData.setImageUrl("http://localhost:8080/article/image/" + updateArticleData.getArticleId());
        articleRepository.save(updateArticleData);
        if (files != null && files.length > 0) {
            Article LastArticle = articleRepository.findLastArticle();
            List<ArticleImage> articleImages = new ArrayList<ArticleImage>();

            for (MultipartFile file : files) {
                System.out.println("file type: " + file.getContentType());
                String imgFile = Base64.getEncoder().encodeToString(file.getBytes());
//                ArticleImage articleImage = new ArticleImage(Long.valueOf(0),imgFile,LastArticle);
                ArticleImage articleImage = new ArticleImage(LastArticle.getArticleId(), imgFile, LastArticle);
                articleImages.add(articleImage);

            }

            articleImageRepository.saveAll(articleImages);
        }

        return new BasicResponseDto("Article updated successfully", true);
    }

    public BasicResponseDto updateArticle(UpdateArticle updateArticle) {
        articleRepoImplement.updateArticleQuery(updateArticle);
        return new BasicResponseDto("Article updated successfully", true);
    }

    @Override
    public BasicResponseDto deleteArticleById(Long id) {
        Article deleteArticle = getArticleById(id);
        articleRepository.delete(deleteArticle);

        return new BasicResponseDto("Article deleted successfully.", true);
    }

    @Override
    public BasicResponseDto deleteAllArticles() {
        articleRepository.deleteAll();
        return new BasicResponseDto("All articles deleted successfully.", true);
    }

    public Article getArticleById(Long id) {
        Article article = articleRepository.findArticleById(id);
        if (article == null) {
            throw new NoSuchElementException("Article not found");
        }
        return article;
    }

    public boolean checkValidDate(String dateStr) {
        String dateFormat = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        try {
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            throw new DateTimeException("Error in date time format: " + e);
        }
    }

    public GeneralUserDto userToGeneralUser(User user) {
        GeneralUserDto generalUser = modelMapper.map(user, GeneralUserDto.class);
        Set<Authority> authorities = user.getAuthorities();
        List<String> authorityNames = authorities.stream()
                .map(Authority::getName)
                .collect(Collectors.toList());
        generalUser.setRole(authorityNames);
        return generalUser;
    }

    public static String extractLastNumber(String url) {
        String regex = ".*/(\\d+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

}

/*

public byte[] getImage(Long articleId) {
    if (articleId == null) {
        throw new NoSuchElementException("ImageId is null");
    }
    List<ArticleImage> articleImages = articleImageRepository.findImagesByArticleId(articleId);

    if(articleImages.isEmpty()) {

    }

    byte[] imageBytes = java.util.Base64.getDecoder().decode(articleImages.get(0).getImage());

    return imageBytes;
}

* */