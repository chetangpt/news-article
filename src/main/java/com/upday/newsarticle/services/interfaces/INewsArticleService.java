package com.upday.newsarticle.services.interfaces;

import com.upday.newsarticle.dto.ArticleDTO;
import com.upday.newsarticle.vo.ArticleVO;

import java.time.LocalDateTime;
import java.util.List;

public interface INewsArticleService {

    ArticleDTO createArticle(ArticleVO article);
    ArticleDTO updateArticle(ArticleVO article, Long articleId);
    void deleteArticle(Long articleId);
    ArticleDTO getArticle(Long articleId);
    List<ArticleDTO> getAllFilteredArticles(String author, LocalDateTime start, LocalDateTime end, String keyword);
}
