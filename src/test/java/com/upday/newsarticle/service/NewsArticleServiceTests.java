package com.upday.newsarticle.service;

import com.upday.newsarticle.dto.ArticleDTO;
import com.upday.newsarticle.exception.NewsArticleException;
import com.upday.newsarticle.mapper.implementation.MapArticleToArticleDTO;
import com.upday.newsarticle.mapper.interfaces.IMapArticleToArticleDTO;
import com.upday.newsarticle.repository.IArticleRepository;
import com.upday.newsarticle.services.implementation.NewsArticleService;
import com.upday.newsarticle.vo.ArticleVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewsArticleServiceTests {

    @InjectMocks
    NewsArticleService newsArticleService;

    @Mock
    IArticleRepository articleRepository;

    @Mock
    IMapArticleToArticleDTO mapArticleToArticleDTO;

    private ArticleVO articleVO = new ArticleVO();
    private ArticleDTO articleDTO = null;


    @Test
    public void createArticleSuccessTest(){
        givenArticleRequestData(articleVO);
        givenMappingServiceReturnsArticleDTO(articleVO);
        givenArticleRepositorySaveArticleReturnDBObject();
        whenCreateArticleServiceIsCalled(articleVO);
        thenVerifyArticleDTOReturnsId();
        thenVerifyArticleRepositorySaveIsCalled();
    }

    @Test
    public void createArticleFailureTest(){
        givenArticleRequestData(articleVO);
        givenMappingServiceReturnsArticleDTO(articleVO);
        givenArticleRepositorySaveArticleReturnPersistenceException();
        assertThrows(NewsArticleException.class, () -> {whenCreateArticleServiceIsCalled(articleVO);});
    }



    private void givenMappingServiceReturnsArticleDTO(ArticleVO articleVO) {
        MapArticleToArticleDTO mapper = new MapArticleToArticleDTO();
        articleDTO = mapper.map(articleVO);
        articleDTO.setArticleId(1L);
        when(mapArticleToArticleDTO.map(Mockito.any())).thenReturn(articleDTO);
    }

    private void thenVerifyArticleRepositorySaveIsCalled() {
        verify(articleRepository, times(1)).save(Mockito.any());
    }

    private void thenVerifyArticleDTOReturnsId() {
        assert(articleDTO.getArticleId()!=null);
    }

    private void whenCreateArticleServiceIsCalled(ArticleVO articleVO) {
        newsArticleService.createArticle(articleVO);
    }

    private void givenArticleRepositorySaveArticleReturnDBObject() {
        when(articleRepository.save(Mockito.any())).thenReturn(articleDTO);
    }

     private void givenArticleRepositorySaveArticleReturnPersistenceException() {
        when(articleRepository.save(Mockito.any())).thenThrow(PersistenceException.class);
    }

    private void givenArticleRequestData(ArticleVO articleVO) {
        articleVO.setHeader("Aus win T20 World Cup 2021");
        articleVO.setShortDescription("Australia are the winners");
        articleVO.setText("Australia are the ultimate champions of the world cup");

        String str = "2021-11-15 19:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        articleVO.setPublishDate(LocalDateTime.parse(str, formatter));

        articleVO.setAuthors("Mark Chapman");
        articleVO.setKeywords("worldcup2021");
    }
}
