package com.grepp.moodlink.app.model.llm;

import com.grepp.moodlink.app.model.data.book.BookRepository;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.movie.MovieRepository;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.music.MusicRepository;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.llm.code.RecommendContentType;
import com.grepp.moodlink.app.model.llm.entity.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final MovieRepository movieRepository;
    private final BookRepository bookRepository;
    private final MusicRepository musicRepository;

    public List<String> getMovies(String keywords) {
        List<String> movies = recommendationRepository.findByKeywordsAndContentType(keywords, RecommendContentType.MOVIE.name())
                .stream()
                .map(Recommendation::getContentId)
                .toList();
        List<Movie> movieEntities = movieRepository.findAllById(movies);
        movieEntities.sort(Comparator.comparing(Movie::getLikeCount).reversed());

        List<String> sortedIds = movieEntities.stream()
                .map(Movie::getId)
                .collect(Collectors.toList());

        return getSortResult(sortedIds);

    }

    public List<String> getBooks(String keywords) {
        List<String> books = recommendationRepository.findByKeywordsAndContentType(keywords, RecommendContentType.BOOK.name())
                .stream()
                .map(Recommendation::getContentId)
                .toList();
        List<Book> bookEntities = bookRepository.findAllByIsbnIn(books);
        bookEntities.sort(Comparator.comparing(Book::getLikeCount).reversed());

        List<String> sortedIds = bookEntities.stream()
                .map(Book::getIsbn)
                .collect(Collectors.toList());

        return getSortResult(sortedIds);
    }

    public List<String> getMusics(String keywords) {
        List<String> musics = recommendationRepository.findByKeywordsAndContentType(keywords, RecommendContentType.MUSIC.name())
                .stream()
                .map(Recommendation::getContentId)
                .toList();
        List<Music> musicEntities = musicRepository.findAllById(musics);
        musicEntities.sort(Comparator.comparing(Music::getLikeCount).reversed());

        List<String> sortedIds = musicEntities.stream()
                .map(Music::getId)
                .collect(Collectors.toList());

        return getSortResult(sortedIds);
    }

    public boolean exists(String keywords) {
        return recommendationRepository.existsByKeywords(keywords);
    }

    public void saveRecommendationContent(List<String> movieids, List<String> bookIds, List<String> musicIds, String keywords, String reason) {
        List<Recommendation> allEntities = new ArrayList<>();

        // 영화 추천 저장
        for (String movieId : movieids) {
            Recommendation entity = Recommendation.builder()
                    .keywords(keywords)
                    .reason(reason)
                    .contentType("MOVIE")
                    .contentId(movieId)
                    .build();
            allEntities.add(entity);
        }
        // 도서 추천 저장
        for (String bookId : bookIds) {
            Recommendation entity = Recommendation.builder()
                    .keywords(keywords)
                    .reason(reason)
                    .contentType("BOOK")
                    .contentId(bookId)
                    .build();
            allEntities.add(entity);
        }
        // 음악 추천 저장
        for (String musicId : musicIds) {
            Recommendation entity = Recommendation.builder()
                    .keywords(keywords)
                    .reason(reason)
                    .contentType("MUSIC")
                    .contentId(musicId)
                    .build();
            allEntities.add(entity);
        }

        recommendationRepository.saveAll(allEntities);
    }

    private static List<String> getSortResult(List<String> sortedIds) {
        List<String> result = new ArrayList<>();
        Random random = new Random();

        result.add(sortedIds.getFirst());

        if (sortedIds.size() > 2) {
            int idx = 1 + random.nextInt(Math.min(2, sortedIds.size() - 1));
            result.add(sortedIds.get(idx));
        }
        if (sortedIds.size() > 4) {
            int idx = 3 + random.nextInt(Math.min(2, sortedIds.size() - 1));
            result.add(sortedIds.get(idx));
        }
        if (sortedIds.size() > 5){
            int start = 5;
            int end = sortedIds.size();
            int idx = start + random.nextInt(end - start);
            result.add(sortedIds.get(idx));
        }
        return result;
    }


    public List<Recommendation> curatingDetailDtoList(String keyword) {
            return recommendationRepository.findByKeywords(keyword);
    }

    public List<Movie> toMovie(List<Recommendation> movieRecommendation) {
        if (movieRecommendation == null || movieRecommendation.isEmpty()) {
            return Collections.emptyList();
        }

        // Recommendation에서 contentId 추출
        List<String> contentIds = movieRecommendation.stream()
                .map(Recommendation::getContentId)
                .collect(Collectors.toList());
        // 해당 contentId로 Movie 조회
        return movieRepository.findAllById(contentIds);
    }

    public List<Book> toBook(List<Recommendation> bookRecommendation) {
        if (bookRecommendation == null || bookRecommendation.isEmpty()) {
            return Collections.emptyList();
        }

        // Recommendation에서 contentId 추출
        List<String> contentIds = bookRecommendation.stream()
                .map(Recommendation::getContentId)
                .collect(Collectors.toList());

        // 해당 contentId로 book 조회
        return bookRepository.findAllById(contentIds);
    }

    public List<Music> toMusic(List<Recommendation> musicRecommendation) {
        if (musicRecommendation == null || musicRecommendation.isEmpty()) {
            return Collections.emptyList();
        }

        // Recommendation에서 contentId 추출
        List<String> contentIds = musicRecommendation.stream()
                .map(Recommendation::getContentId)
                .collect(Collectors.toList());

        // 해당 contentId로 music 조회
        return musicRepository.findAllById(contentIds);
    }

    public List<Movie> bestMovies() {
        return movieRepository.findTop10ByOrderByLikeCountDesc();

    }

    public List<Book> bestBooks() {
        return bookRepository.findTop10ByOrderByLikeCountDesc();
    }

    public List<Music> bestMusics() {
        return musicRepository.findTop10ByOrderByLikeCountDesc();
    }
}
