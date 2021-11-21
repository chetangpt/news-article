package com.upday.newsarticle.services.implementation;

import com.upday.newsarticle.constants.NewsArticleConstants;
import com.upday.newsarticle.dto.ArticleDTO;
import com.upday.newsarticle.exception.NewsArticleException;
import com.upday.newsarticle.mapper.interfaces.IMapArticleToArticleDTO;
import com.upday.newsarticle.repository.IArticleRepository;
import com.upday.newsarticle.services.interfaces.INewsArticleService;
import com.upday.newsarticle.vo.ArticleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class NewsArticleService implements INewsArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsArticleService.class);

    @Autowired
    IArticleRepository articleRepository;

    @Autowired
    IMapArticleToArticleDTO mapArticleToArticleDTO;

    @Override
    public ArticleDTO createArticle(ArticleVO article) {
        try {
            ArticleDTO articleDTO = articleRepository.save(mapArticleToArticleDTO.map(article));
            LOGGER.debug("Article created with article id: {}", articleDTO.getArticleId());
            return articleDTO;
        } catch (Exception e) {
            LOGGER.error("Error captured while persisting the data: {}", e.getMessage());
            throw new NewsArticleException(NewsArticleConstants.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public ArticleDTO updateArticle(ArticleVO article, Long articleId) {
        try {
            ArticleDTO articleDTO = articleRepository.findByArticleId(articleId);
            if (articleDTO == null) {
                throw new NewsArticleException(HttpStatus.BAD_REQUEST.toString(), "No data found for the articleId provided");
            }
            ArticleDTO updatedArticle = mapArticleToArticleDTO.map(article);
            updatedArticle.setArticleId(articleDTO.getArticleId());
            articleRepository.save(updatedArticle);
            return updatedArticle;
        } catch (Exception e) {
            LOGGER.error("Error captured while updating the data: {}", e.getMessage());
            throw new NewsArticleException(NewsArticleConstants.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteArticle(Long articleId) {
        try {
            ArticleDTO articleDTO = articleRepository.findByArticleId(articleId);
            if (articleDTO == null) {
                throw new NewsArticleException("400", "No data found for the articleId provided");
            }
            articleRepository.delete(articleDTO);
        } catch (Exception e) {
            LOGGER.error("Error captured while deleting the data: {}", e.getMessage());
            throw new NewsArticleException(NewsArticleConstants.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public ArticleDTO getArticle(Long articleId) {
        try {
            ArticleDTO articleDTO = articleRepository.findByArticleId(articleId);
            return articleDTO;
        } catch (Exception e) {
            LOGGER.error("Error captured while extracting the data: {}", e.getMessage());
            throw new NewsArticleException(NewsArticleConstants.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public List<ArticleDTO> getAllFilteredArticles(String author, LocalDateTime start, LocalDateTime end, String keyword) {
        List<ArticleDTO> articles =null;
        if (areAllNotNull(author, start, end, keyword)) {
            return articleRepository.findByAuthorsContainingAndKeywordsContainingAndPublishDateBetween(
                    author, keyword, start, end);
        }

        if (areAllNull(author, start, end, keyword)) {
            return articleRepository.findAll();
        }

        if (author != null) {
            return articleRepository.findByAuthorsContaining(author);
        }

        if (start == null && end != null) {
            throw new NewsArticleException(NewsArticleConstants.BAD_REQUEST, "There is a endDate without a startDate");
        } else if (start != null && end == null) {
            end = LocalDateTime.now();
            return articleRepository.findByPublishDateBetween(start, end);
        } else if (start != null && end != null) {
            return articleRepository.findByPublishDateBetween(start, end);
        }

        if (keyword != null) {
            return articleRepository.findByKeywordsContaining(keyword);
        }
        return articles;
    }

    private static boolean areAllNotNull(Object... objects) {
        return Stream.of(objects).allMatch(Objects::nonNull);
    }

    private static boolean areAllNull(Object... objects) {
        return Stream.of(objects).allMatch(Objects::isNull);
    }
}
