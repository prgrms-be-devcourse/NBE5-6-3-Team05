package com.grepp.moodlink.app.controller.web.home;

import com.grepp.moodlink.app.model.auth.domain.Principal;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.home.HomeService;
import java.util.List;
import java.util.Objects;

import com.grepp.moodlink.app.model.keyword.KeywordService;
import com.grepp.moodlink.app.model.llm.RecommendationService;
import com.grepp.moodlink.app.model.llm.code.RecommendContentType;
import com.grepp.moodlink.app.model.llm.entity.Recommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final KeywordService keywordService;
    private final RecommendationService recommendationService;

    @GetMapping
    public String mainPage(Model model) {
        List<String> thumbnail = homeService.showContent();
        List<String> people = homeService.findPeople();
        List<String> title = homeService.findTitle();

        String userId = getLoginUserId();
        String keyword = "";
        List<Recommendation> recommendations;

        if (userId != null) {
            keyword = keywordService.findKeywords(userId);
            if (keyword != null) {
                recommendations = recommendationService.curatingDetailDtoList(keyword);
                List<Recommendation> movieRecommendation = recommendations.stream().filter(
                        r -> r.getContentType().equals(RecommendContentType.MOVIE.name())
                ).toList();
                List<Recommendation> bookRecommendation = recommendations.stream().filter(
                        r -> r.getContentType().equals(RecommendContentType.BOOK.name())
                ).toList();
                List<Recommendation> musicRecommendation = recommendations.stream().filter(
                        r -> r.getContentType().equals(RecommendContentType.MUSIC.name())
                ).toList();

                List<Movie> movies = recommendationService.toMovie(movieRecommendation);
                List<Book> books = recommendationService.toBook(bookRecommendation);
                List<Music> musics = recommendationService.toMusic(musicRecommendation);

                model.addAttribute("movies", movies);
                model.addAttribute("books", books);
                model.addAttribute("musics", musics);
            }
        }
        if(userId == null || Objects.requireNonNull(keyword).equals("No exist keywords")){
            List<Movie> movies = recommendationService.bestMovies();
            List<Book> books = recommendationService.bestBooks();
            List<Music> musics = recommendationService.bestMusics();

            model.addAttribute("movies", movies);
            model.addAttribute("books", books);
            model.addAttribute("musics", musics);
        }

        model.addAttribute("thumbnail", thumbnail);
        model.addAttribute("people", people);
        model.addAttribute("title", title);

        return "/home/mainPage";
    }

    @GetMapping("/search")
    public String searchContent(Model model, @RequestParam String contentName) {
        List<MusicDto> music = homeService.searchMusicContent(contentName);
        List<MovieDto> movie = homeService.searchMovieContent(contentName);
        List<BookDto> book = homeService.searchBookContent(contentName);

        model.addAttribute("contentName", contentName);
        model.addAttribute("music", music);
        model.addAttribute("movie", movie);
        model.addAttribute("book", book);

        return "/home/search";
    }

    @GetMapping("/search/genre")
    public String searchGenre(Model model, @RequestParam String genreName) {
        List<MusicDto> musicContentByGenre = homeService.searchMusicContentByGenre(genreName);
        model.addAttribute("musicContentByGenre", musicContentByGenre);
        return"/home/search";
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
