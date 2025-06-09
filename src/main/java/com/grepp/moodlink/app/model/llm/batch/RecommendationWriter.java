package com.grepp.moodlink.app.model.llm.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class RecommendationWriter implements ItemWriter<RecommendationDto> {

    @Override
    public void write(Chunk<? extends RecommendationDto> chunk) throws Exception {
        for (RecommendationDto result : chunk) {
            System.out.println("키워드: " + result.getKeywords());
            System.out.println("추천 이유: " + result.getReason());
            System.out.println("영화 추천: " + result.getMovieRecommendations());
            System.out.println("도서 추천: " + result.getBookRecommendations());
            System.out.println("음악 추천: " + result.getMusicRecommendations());

        }
    }
}
