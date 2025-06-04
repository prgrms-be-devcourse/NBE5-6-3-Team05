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
        System.out.println(recommendationService.getBooks("기쁨|맑음|아침|가족|즐거움"));
    }
}