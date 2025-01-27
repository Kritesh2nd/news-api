package com.exm.news.service;

import com.exm.news.entity.app.NewsView;
import com.exm.news.repository.app.NewsViewRepository;
import com.exm.news.response.BasicResponseDto;
import com.exm.news.service.interfaces.NewsViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class NewsViewServiceImpl implements NewsViewService {

    @Autowired
    private NewsViewRepository newsViewRepository;

    @Override
    public BasicResponseDto addToNewsView(Long articleId, String side) {
        if (!validSide(side)) throw new NoSuchElementException("News Side not found");

        newsViewRepository.save(new NewsView(articleId, side));
        return new BasicResponseDto("Article added to news view. side: "+side,true);
    }

    @Override
    public List<NewsView> listNewsViewBySide(String side) {
        if (!validSide(side)) throw new NoSuchElementException("News Side not found");

        List<NewsView>  newsViewList = newsViewRepository.findBySide(side);

        if(newsViewList.isEmpty()) return List.of();

        return newsViewList;
    }

    @Override
    public BasicResponseDto removeFromNewsView(Long articleId, String side) {
        if (!validSide(side)) throw new NoSuchElementException("News Side not found");

        Optional<NewsView> newsView =  newsViewRepository.findByArticleIdAndSide(articleId, side);
        if(newsView.isEmpty()) throw new NoSuchElementException("News View not found");

        newsViewRepository.delete(newsView.get());
        return new BasicResponseDto("Article removed from news view. side: "+side,true);
    }

    public boolean validSide(String inputSide) {
        String[] sides = {"left", "middle", "right", "visual story", "latest update", "public pulse", "detail side"};
        boolean found = false;
        for (String side : sides) {
            if (side.equalsIgnoreCase(inputSide)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
