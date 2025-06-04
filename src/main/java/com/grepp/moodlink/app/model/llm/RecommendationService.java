package com.grepp.moodlink.app.model.llm;

import com.grepp.moodlink.app.model.llm.code.ContentType;
import com.grepp.moodlink.app.model.llm.entity.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public List<String> getMovies(String keywords) {
        return recommendationRepository.findByKeywordsAndContentType(keywords, ContentType.MOVIE.name())
                .stream()
                .map(Recommendation::getContentId)
                .toList();
    }

    public List<String> getBooks(String keywords) {
        return recommendationRepository.findByKeywordsAndContentType(keywords, ContentType.BOOK.name())
                .stream()
                .map(Recommendation::getContentId)
                .toList();
    }

    public List<String> getMusics(String keywords) {
        return recommendationRepository.findByKeywordsAndContentType(keywords, ContentType.MUSIC.name())
                .stream()
                .map(Recommendation::getContentId)
                .toList();
    }
}
