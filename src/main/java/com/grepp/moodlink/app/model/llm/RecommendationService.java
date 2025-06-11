package com.grepp.moodlink.app.model.llm;

import com.grepp.moodlink.app.model.llm.code.ContentType;
import com.grepp.moodlink.app.model.llm.entity.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public List<String> getMovies(String keywords) {
        List<String> movies = recommendationRepository.findByKeywordsAndContentType(keywords, ContentType.MOVIE.name())
                .stream()
                .map(Recommendation::getContentId)
                .toList();
        if(movies.size() < 5) return movies;
        else {
            // 5개 이상인 경우 4개를 무작위로 추출
            List<String> copy = new ArrayList<>(movies);
            Collections.shuffle(copy);
            return copy.subList(0, 4);
        }
    }

    public List<String> getBooks(String keywords) {
        List<String> books = recommendationRepository.findByKeywordsAndContentType(keywords, ContentType.BOOK.name())
                .stream()
                .map(Recommendation::getContentId)
                .toList();
        if(books.size() < 5) return books;
        else {
            // 5개 이상인 경우 4개를 무작위로 추출
            List<String> copy = new ArrayList<>(books);
            Collections.shuffle(copy);
            return copy.subList(0, 4);
        }
    }

    public List<String> getMusics(String keywords) {
        List<String> musics = recommendationRepository.findByKeywordsAndContentType(keywords, ContentType.MUSIC.name())
                .stream()
                .map(Recommendation::getContentId)
                .toList();
        if(musics.size() < 5) return musics;
        else {
            // 5개 이상인 경우 4개를 무작위로 추출
            List<String> copy = new ArrayList<>(musics);
            Collections.shuffle(copy);
            return copy.subList(0, 4);
        }
    }

    public boolean exists(String keywords) {
        return recommendationRepository.existsByKeywords(keywords);
    }
}
