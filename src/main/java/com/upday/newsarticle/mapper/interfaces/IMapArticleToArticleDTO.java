package com.upday.newsarticle.mapper.interfaces;

import com.upday.newsarticle.dto.ArticleDTO;
import com.upday.newsarticle.vo.ArticleVO;

public interface IMapArticleToArticleDTO {

    public ArticleDTO map (ArticleVO article);

}
