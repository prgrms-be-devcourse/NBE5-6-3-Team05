package com.grepp.moodlink.app.controller.web.recommend;

import com.grepp.moodlink.app.model.auth.domain.Principal;
import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.movie.GenreRepository;
import com.grepp.moodlink.app.model.data.movie.MovieService;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.music.MusicService;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.embedding.EmbeddingService;
import com.grepp.moodlink.app.model.keyword.KeywordService;
import jakarta.servlet.http.HttpSession;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/recommend")
@Controller
@RequiredArgsConstructor
public class RecommendController {

    private final EmbeddingService embeddingService;
    private final KeywordService keywordService;
    private final GenreRepository genreRepository;
    private final MovieService movieService;
    private final BookService bookService;
    private final MusicService musicService;

    @GetMapping
    public String selectKeyword(){
        return "/recommend/recommend";
    }

    @ModelAttribute("genres")
    public List<Genre> populateGenres() {
        return genreRepository.findAll();
    }

    @PostMapping("result")
    public String selectKeyword(
            @RequestParam("genre") String genre,
            @RequestParam("keywords") String keywords,
            HttpSession session){
        genre = genre.substring(1);

        String userId = getLoginUserId();
        processUserKeywords(userId, keywords);

        RecommendationDto recommendations = generateRecommendations(userId, genre);
        setSessionAttributes(session, recommendations);

        return "home/mainPage";
    }

    private void processUserKeywords(String userId, String keywords) {
        keywordService.generateKeywordSelection(userId);
        embeddingService.generateEmbeddingKeyword(userId, keywords);
    }

    private RecommendationDto generateRecommendations(String userId, String genre) {
        return RecommendationDto.builder()
                .movies(getMovieRecommendations(genre, userId))
                .books(getBookRecommendations(userId))
                .musics(getMusicRecommendations(userId))
                .build();
    }

    private List<Movie> getMovieRecommendations(String genre, String userId) {
        String movieResult = embeddingService.recommendMovie(genre, userId);
        return movieService.parseRecommend(movieResult);
    }

    private List<Book> getBookRecommendations(String userId) {
        String bookResult = embeddingService.recommendBook(userId);
        return bookService.parseRecommend(bookResult);
    }

    private List<Music> getMusicRecommendations(String userId) {
        String musicResult = embeddingService.recommendMusic(userId);
        return musicService.parseRecommend(musicResult);
    }

    private void setSessionAttributes(HttpSession session, RecommendationDto dto) {
        session.setAttribute("movies", dto.getMovies());
        session.setAttribute("books", dto.getBooks());
        session.setAttribute("musics", dto.getMusics());
    }


    private String getLoginUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        Principal principal = (Principal) auth.getPrincipal();
        return principal.getUsername();
    }

    @Data
    @Builder
    public static class RecommendationDto {
        private List<Movie> movies;
        private List<Book> books;
        private List<Music> musics;
    }
}
