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
import com.grepp.moodlink.app.model.llm.EmbeddingService;
import com.grepp.moodlink.app.model.keyword.KeywordService;
import com.grepp.moodlink.app.model.llm.LlmService;
import com.grepp.moodlink.app.model.result.CuratingDetailRepository;
import com.grepp.moodlink.app.model.result.dto.CuratingDetailDto;
import com.grepp.moodlink.app.model.result.dto.CuratingDetailIdDto;
import com.grepp.moodlink.app.model.result.entity.CuratingDetail;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final LlmService llmService;
    private final CuratingDetailRepository curatingDetailRepository;

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

        String reason = llmService.generateReason(userId);
        System.out.println(reason);
        session.setAttribute("reason", reason);

        List<CuratingDetailIdDto> items = curatingContents(genre, userId);
        System.out.println(items);
        session.setAttribute("items", items);


        return "redirect:/result";
    }

    private List<CuratingDetailIdDto> curatingContents(String genre, String userId) {
        List<CuratingDetailIdDto> details = new ArrayList<>();
        List<String> movieIds = getMovieRecommendations(genre, userId);
        List<String> bookIds = getBookRecommendations(userId);
        List<String> musicIds = getMusicRecommendations(userId);
        for(int i = 0; i < musicIds.size(); i++){
            CuratingDetailIdDto detail = new CuratingDetailIdDto();
            detail.setMovieId(movieIds.get(i));
            detail.setBookId(bookIds.get(i));
            detail.setSongId(musicIds.get(i));
            details.add(detail);
        }
        return details;
    }

    private void processUserKeywords(String userId, String keywords) {
        keywordService.generateKeywordSelection(userId);
        embeddingService.generateEmbeddingKeyword(userId, keywords);
    }


    private List<String> getMovieRecommendations(String genre, String userId) {
        String movieResult = llmService.recommendMovie(genre, userId);
        return movieService.parseRecommend(movieResult);
    }

    private List<String> getBookRecommendations(String userId) {
        String bookResult = llmService.recommendBook(userId);
        return bookService.parseRecommend(bookResult);
    }

    private List<String> getMusicRecommendations(String userId) {
        String musicResult = llmService.recommendMusic(userId);
        return musicService.parseRecommend(musicResult);
    }


    private String getLoginUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        Principal principal = (Principal) auth.getPrincipal();
        return principal.getUsername();
    }


}
