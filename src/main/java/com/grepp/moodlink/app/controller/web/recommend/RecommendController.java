package com.grepp.moodlink.app.controller.web.recommend;

import com.grepp.moodlink.app.model.auth.domain.Principal;
import com.grepp.moodlink.app.model.data.movie.GenreRepository;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.embedding.EmbeddingService;
import com.grepp.moodlink.app.model.keyword.KeywordService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;


@RequestMapping("/recommend")
@Controller
@RequiredArgsConstructor
public class RecommendController {

    private final EmbeddingService embeddingService;
    private final KeywordService keywordService;
    private final GenreRepository genreRepository;

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
        String userId = getLoginUserId();
        keywordService.generateKeywordSelection(userId);
        embeddingService.generateEmbeddingKeyword(userId, keywords);
        genre = genre.substring(1);
        session.setAttribute("movies", embeddingService.cosineComputeMovie(genre, userId));
        session.setAttribute("books", embeddingService.cosineComputeBook(userId));
        session.setAttribute("musics", embeddingService.cosineComputeMusic(userId));
        return "home/mainPage";
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
