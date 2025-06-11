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
        System.out.println(recommendationService.getMovies("기쁨|맑음|아침|가족|즐거움"));
        System.out.println(recommendationService.getBooks("위로|흐림|저녁|혼자|휴식"));
        System.out.println(recommendationService.getMusics("그리움|흐림|밤|혼자|위로"));
    }
}