package com.grepp.moodlink.app.controller.web.home;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.home.HomeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public String mainPage(Model model) {
        List<String> thumbnail = homeService.showContent();
        List<String> people = homeService.findPeople();
        List<String> title = homeService.findTitle();

        model.addAttribute("thumbnail", thumbnail);
        model.addAttribute("people", people);
        model.addAttribute("title", title);

        return "/home/mainPage";
    }

    @GetMapping("/worldcup")
    public String worldcupPage(Model model) {
        return "/home/worldcup";
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
}
