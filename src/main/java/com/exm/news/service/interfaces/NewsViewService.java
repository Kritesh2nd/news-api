package com.exm.news.service.interfaces;

import com.exm.news.entity.app.NewsView;
import com.exm.news.response.BasicResponseDto;

import java.util.List;

public interface NewsViewService {

    BasicResponseDto addToNewsView(Long articleId, String side);

    List<NewsView> listNewsViewBySide(String side);

    BasicResponseDto removeFromNewsView(Long articleId, String side);

}



/*


#c
add to news view (newsid, side)

#r
list news view by side

#u

#d
remove from news view


*/