package com.grepp.moodlink.app.model.llm.batch;

import com.grepp.moodlink.app.model.llm.RecommendationRepository;
import com.grepp.moodlink.app.model.llm.entity.Recommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendationWriter implements ItemWriter<RecommendationDto> {

    private final RecommendationRepository recommendationRepository;

    @Override
    public void write(Chunk<? extends RecommendationDto> chunk) {

        List<Recommendation> allEntities = new ArrayList<>();

        for (RecommendationDto dto : chunk) {
            if (dto.getReason().isEmpty()) continue;

            // 영화 추천 저장
            for (String movieId : dto.getMovieRecommendations()) {
                Recommendation entity = Recommendation.builder()
                        .keywords(dto.getKeywords())
                        .reason(dto.getReason())
                        .contentType("MOVIE")
                        .contentId(movieId)
                        .build();
                allEntities.add(entity);
            }
            // 도서 추천 저장
            for (String bookId : dto.getBookRecommendations()) {
                Recommendation entity = Recommendation.builder()
                        .keywords(dto.getKeywords())
                        .reason(dto.getReason())
                        .contentType("BOOK")
                        .contentId(bookId)
                        .build();
                allEntities.add(entity);
            }
            // 음악 추천 저장
            for (String musicId : dto.getMusicRecommendations()) {
                Recommendation entity = Recommendation.builder()
                        .keywords(dto.getKeywords())
                        .reason(dto.getReason())
                        .contentType("MUSIC")
                        .contentId(musicId)
                        .build();
                allEntities.add(entity);
            }
        }
        recommendationRepository.saveAll(allEntities);
    }
}
