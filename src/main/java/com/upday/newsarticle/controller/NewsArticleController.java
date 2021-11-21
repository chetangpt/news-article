package com.upday.newsarticle.controller;

import com.upday.newsarticle.dto.ArticleDTO;
import com.upday.newsarticle.exception.NewsArticleException;
import com.upday.newsarticle.services.interfaces.INewsArticleService;
import com.upday.newsarticle.vo.ArticleVO;
import com.upday.newsarticle.vo.CommonResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Chetan Gupta
 */

@RestController
@RequestMapping("news/v1")
public class NewsArticleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsArticleController.class);

    @Autowired
    INewsArticleService newsArticleService;

    /**
     * POST call to create article
     */
    @RequestMapping(
            value = "/article",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createArticle(@RequestBody ArticleVO article) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        try {
            LOGGER.info("Creating article using POST call");
            ArticleDTO articleDTO = newsArticleService.createArticle(article);
            return new ResponseEntity<>(articleDTO, headers, HttpStatus.CREATED);
        } catch (NewsArticleException e) {
            return createCustomException(e);
        } catch (Exception e) {
            return createCommonException(e);
        }
    }


    /**
     * PUT call to update article
     */
    @RequestMapping(
            value = "/article/{articleId}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateArticle(@RequestBody ArticleVO article, @PathVariable Long articleId) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        try {
            LOGGER.info("Updating article using PUT call");
            ArticleDTO articleDTO = newsArticleService.updateArticle(article, articleId);
            return new ResponseEntity<>(articleDTO, headers, HttpStatus.OK);
        } catch (NewsArticleException e) {
            return createCustomException(e);
        } catch (Exception e) {
            return createCommonException(e);
        }
    }

    /**
     * Delete call to delete article from articleId
     */
    @RequestMapping(
            value = "/article/{articleId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteArticle(@PathVariable Long articleId) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        try {
            LOGGER.info("Deleting article using DELETE call");
            newsArticleService.deleteArticle(articleId);
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        } catch (NewsArticleException e) {
            return createCustomException(e);
        } catch (Exception e) {
            return createCommonException(e);
        }
    }

    /**
     * GET call to get article from articleId
     */
    @RequestMapping(
            value = "/article/{articleId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getArticle(@PathVariable Long articleId) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        try {
            LOGGER.debug("Getting article using GET call for articleId: {}", articleId);
            ArticleDTO articleDTO = newsArticleService.getArticle(articleId);
            return new ResponseEntity<>(articleDTO, headers, HttpStatus.OK);
        } catch (NewsArticleException e) {
            return createCustomException(e);
        } catch (Exception e) {
            return createCommonException(e);
        }
    }

    /**
     * GET call to get all articles using filter like authors, dates and keywords
     */
    @RequestMapping(
            value = "/article",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getArticles(@RequestParam(value = "author", required = false) String author,
                                              @RequestParam(value = "startDate", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                              @RequestParam(value = "endDate", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                              @RequestParam(value = "keyword", required = false) String keyword) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        try {
            List<ArticleDTO> articleDTO = newsArticleService.getAllFilteredArticles(author, start, end, keyword);
            return new ResponseEntity<>(articleDTO, headers, HttpStatus.OK);
        } catch (NewsArticleException e) {
            return createCustomException(e);
        } catch (Exception e) {
            return createCommonException(e);
        }
    }

    private ResponseEntity<Object> createCommonException(Exception e) {
        CommonResponseVO commonResponseVO;
        commonResponseVO = new CommonResponseVO();
        commonResponseVO.setStatusCode(500);
        commonResponseVO.setStatus("Failure");
        commonResponseVO.setStatusMessage(e.getMessage());
        return new ResponseEntity<>(commonResponseVO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> createCustomException(NewsArticleException e) {
        CommonResponseVO commonResponseVO = new CommonResponseVO();
        commonResponseVO.setStatusCode(Integer.parseInt(e.getErrCode()));
        commonResponseVO.setStatus("Failure");
        commonResponseVO.setStatusMessage(e.getErrMsg());
        return new ResponseEntity<>(commonResponseVO, HttpStatus.resolve(Integer.parseInt(e.getErrCode())));
    }

}
