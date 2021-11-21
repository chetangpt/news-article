package com.upday.newsarticle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.upday.newsarticle.vo.ArticleVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
public class NewsArticleIntegrationTests {


    private MockMvc mockMvc;
    ObjectMapper mapper = JsonMapper.builder()
        .findAndAddModules()
        .build();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenGetArticleCall_whenMockMVC_thenResponseOK() throws Exception {
        this.mockMvc
                .perform(get("/news/v1/article/{articleId}", 2))
                .andDo(print()).andExpect(status().isOk())

                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.header").value("Aus win T20 World Cup 2021"));
    }

    @Test
    public void givenPostArticleCall_whenMockMVC_thenResponseCreated() throws Exception {

        ArticleVO article = mapper
                .readValue(Paths.get("src/test/resources/postcall.json").toFile(),
                        ArticleVO.class);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.post("/news/v1/article")
                        .content(mapper.writeValueAsString(article))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.articleId").exists());

    }

}
