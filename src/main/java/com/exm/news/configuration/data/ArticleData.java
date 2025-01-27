package com.exm.news.configuration.data;

import com.exm.news.entity.app.Article;
import com.exm.news.entity.app.Category;
import com.exm.news.entity.app.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArticleData {

    List<Article> articleList = new ArrayList<>();

    public ArticleData(){
        articleList.add(new Article(
                "Stock Market Hits Record Highs Amid Economic Optimism",
                "Markets surge as investors respond to positive economic data.",
                "https://example.com/images/stock-market.jpg",
                "The stock market reached new heights today, fueled by strong economic reports and investor confidence in future growth. "
                        + "Analysts credit the rally to an increase in consumer spending, strong job reports, and positive quarterly earnings from major corporations. "
                        + "Investors are particularly optimistic about the technology and energy sectors, which have shown remarkable growth in recent months. "
                        + "Despite some concerns about inflation and interest rate adjustments, experts believe the market will continue its upward trajectory if economic indicators remain strong.",
                LocalDateTime.now().minusDays(1),
                new User(""),
                new Category("")
        ));

    }
}
