package com.grepp.moodlink.app.model.llm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecommendationServiceTest {
    @Autowired
    private RecommendationService recommendationService;


    @Test
    public void recommendTest(){
        System.out.println(recommendationService.getMovies("오늘 기분은=기쁨,즐거움이야.오늘 날씨는=눈이야.지금 시간대는=저녁이야.같이 있는 사람은=가족이야.지금 필요한거는=즐거움"));
        System.out.println(recommendationService.getBooks("오늘 기분은=기쁨,즐거움이야.오늘 날씨는=눈이야.지금 시간대는=저녁이야.같이 있는 사람은=가족이야.지금 필요한거는=즐거움"));
        System.out.println(recommendationService.getMusics("오늘 기분은=기쁨,즐거움이야.오늘 날씨는=눈이야.지금 시간대는=저녁이야.같이 있는 사람은=가족이야.지금 필요한거는=즐거움"));
    }
}