package com.grepp.moodlink.app.controller.web.recommend;

import com.grepp.moodlink.app.model.auth.domain.Principal;
import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.movie.GenreRepository;
import com.grepp.moodlink.app.model.data.movie.MovieService;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.music.MusicService;
import com.grepp.moodlink.app.model.keyword.KeywordService;
import com.grepp.moodlink.app.model.llm.LlmService;
import com.grepp.moodlink.app.model.llm.RecommendationService;
import com.grepp.moodlink.app.model.member.MemberService;
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
    private final MovieService movieService;
    private final BookService bookService;
    private final MusicService musicService;
    private final LlmService llmService;

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
        @RequestParam("keywords") String keywords,
        HttpSession session) {
        System.out.println(keywords);
        String reason;
        // 키워드가 존재하는지
        if(keywordService.exist(keywords)) {
            // 키워드가 존재한다면 추천 받은적 있는지
             if(keywordService.findReason(keywords) == null) {
                 reason = llmService.generateReason(keywords);
             }else{
                 reason = keywordService.findReason(keywords);
             }
        }else{ // 없다면 새로 생성
            processKeyword(keywords);
            reason = llmService.generateReason(keywords);
        }
        String userId = getLoginUserId();
        if (userId != null) memberService.selectKeyword(userId, keywords);

        session.setAttribute("reason", reason);

        List<CuratingDetailIdDto> items = curatingContents(keywords, reason);

        session.setAttribute("items", items);

        return "redirect:/result";
    }

    private void processKeyword(String keywords) {
        keywordService.generateKeywordSelection(keywords);
    }

    private List<CuratingDetailIdDto> curatingContents(String keywords, String reason) {
        List<CuratingDetailIdDto> details = new ArrayList<>();
        List<String> movieIds;
        List<String> bookIds;
        List<String> musicIds;
        if(recommendationService.exists(keywords) && recommendationService.isActivated(keywords)) {// 추천 받은 적 있는 키워드의 경우 가져오기
            movieIds = getMovieRecommendations(keywords);
            bookIds = getBookRecommendations(keywords);
            musicIds = getMusicRecommendations(keywords);
        }else{ // 추천 받은 적 없는 키워드의 경우 새로 생성
            movieIds = generateMovieRecommendations(keywords);
            bookIds = generateBookRecommendations(keywords);
            musicIds = generateMusicRecommendations(keywords);
            saveRecommendation(movieIds, bookIds, musicIds, keywords, reason);
        }
        for (int i = 0; i < 4; i++) {
            CuratingDetailIdDto detail = new CuratingDetailIdDto();
            detail.setMovieId(movieIds.get(i));
            detail.setBookId(bookIds.get(i));
            detail.setSongId(musicIds.get(i));
            details.add(detail);
        }
        return details;
    }

    private void saveRecommendation(List<String> movieIds, List<String> bookIds, List<String> musicIds, String keywords, String reason) {
        recommendationService.saveRecommendationContent(movieIds, bookIds, musicIds, keywords, reason);
    }

    private List<String> generateMovieRecommendations(String keywords) {
        String movieResult = llmService.recommendMovie(keywords);
        return movieService.parseRecommend(movieResult);
    }

    private List<String> generateBookRecommendations(String keywords) {
        String bookResult = llmService.recommendBook(keywords);
        return bookService.parseRecommend(bookResult);
    }

    private List<String> generateMusicRecommendations(String keywords) {
        String musicResult = llmService.recommendMusic(keywords);
        return musicService.parseRecommend(musicResult);
    }

    private List<String> getMovieRecommendations(String keywords) {
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
