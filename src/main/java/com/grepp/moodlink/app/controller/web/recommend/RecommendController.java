package com.grepp.moodlink.app.controller.web.recommend;

import com.grepp.moodlink.app.model.auth.domain.Principal;
import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.movie.GenreRepository;
import com.grepp.moodlink.app.model.data.movie.MovieService;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.music.MusicService;
import com.grepp.moodlink.app.model.keyword.KeywordService;
import com.grepp.moodlink.app.model.llm.EmbeddingService;
import com.grepp.moodlink.app.model.llm.LlmService;
import com.grepp.moodlink.app.model.llm.RecommendationService;
import com.grepp.moodlink.app.model.member.MemberRepository;
import com.grepp.moodlink.app.model.member.MemberService;
import com.grepp.moodlink.app.model.result.CuratingDetailRepository;
import com.grepp.moodlink.app.model.result.dto.CuratingDetailIdDto;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/recommend")
@Controller
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendationService recommendationService;
    private final KeywordService keywordService;
    private final GenreRepository genreRepository;
    private final MemberService memberService;

    @GetMapping
    public String selectKeyword() {
        return "/recommend/recommend";
    }

    @ModelAttribute("userGenre")
    public String findUserGenre(){
        String userId = getLoginUserId();
        if(userId == null){
            return "";
        }
        return memberService.findGenre(userId).orElseThrow().getGenre();
    }

    @ModelAttribute("genres")
    public List<Genre> populateGenres() {
        return genreRepository.findAll();
    }

    @PostMapping("result")
    public String selectKeyword(
        @RequestParam("genre") String genre,
        @RequestParam("keywords") String keywords,
        HttpSession session) {
        genre = genre.substring(1);

        String reason = keywordService.findReason(keywords);
        session.setAttribute("reason", reason);
        System.out.println(reason);

        List<CuratingDetailIdDto> items = curatingContents(genre, keywords);
        System.out.println(items);
        session.setAttribute("items", items);

        return "redirect:/result";
    }

    private List<CuratingDetailIdDto> curatingContents(String genre, String keywords) {
        List<CuratingDetailIdDto> details = new ArrayList<>();
        List<String> movieIds = getMovieRecommendations(genre, keywords);
        List<String> bookIds = getBookRecommendations(keywords);
        List<String> musicIds = getMusicRecommendations(keywords);
        for (int i = 0; i < musicIds.size(); i++) {
            CuratingDetailIdDto detail = new CuratingDetailIdDto();
            detail.setMovieId(movieIds.get(i));
            detail.setBookId(bookIds.get(i));
            detail.setSongId(musicIds.get(i));
            details.add(detail);
        }
        return details;
    }

    private List<String> getMovieRecommendations(String genre, String keywords) {
        return recommendationService.getMovies(keywords);
    }

    private List<String> getBookRecommendations(String keywords) {
        return recommendationService.getBooks(keywords);
    }

    private List<String> getMusicRecommendations(String keywords) {
        return recommendationService.getMusics(keywords);
    }


    private String getLoginUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(
            auth.getPrincipal())) {
            return null;
        }
        Principal principal = (Principal) auth.getPrincipal();
        return principal.getUsername();
    }


}
