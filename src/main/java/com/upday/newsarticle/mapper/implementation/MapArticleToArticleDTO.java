package com.upday.newsarticle.mapper.implementation;

import com.upday.newsarticle.dto.ArticleDTO;
import com.upday.newsarticle.mapper.interfaces.IMapArticleToArticleDTO;
import com.upday.newsarticle.vo.ArticleVO;
import org.springframework.stereotype.Component;

@Component
public class MapArticleToArticleDTO implements IMapArticleToArticleDTO {


    @Override
    public ArticleDTO map(ArticleVO article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setHeader(article.getHeader());
        articleDTO.setShortDescription(article.getShortDescription());
        articleDTO.setText(article.getText());
        articleDTO.setPublishDate(article.getPublishDate());
        articleDTO.setAuthors(article.getAuthors());
        articleDTO.setKeywords(article.getKeywords());
        return articleDTO;
    }
}
