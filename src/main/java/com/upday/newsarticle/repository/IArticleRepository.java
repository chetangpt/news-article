package com.upday.newsarticle.repository;

import com.upday.newsarticle.dto.ArticleDTO;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IArticleRepository extends CrudRepository<ArticleDTO,Long> {

    ArticleDTO findByArticleId(Long articleId);
    List<ArticleDTO> findAll();
    List<ArticleDTO> findByAuthorsContaining(String author);
    List<ArticleDTO> findByKeywordsContaining(String keyword);
    List<ArticleDTO> findByPublishDateBetween(LocalDateTime start, LocalDateTime end);
    List<ArticleDTO> findByAuthorsContainingAndKeywordsContainingAndPublishDateBetween(String author, String keyword, LocalDateTime start, LocalDateTime end);

}
