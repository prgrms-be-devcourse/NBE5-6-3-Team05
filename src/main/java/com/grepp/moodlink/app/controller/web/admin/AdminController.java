package com.grepp.moodlink.app.controller.web.admin;

import com.grepp.moodlink.app.controller.web.admin.payload.MovieAddRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    // 영화 리스트를 모두 보여주는 화면
    // 관리자 페이지의 기본화면
    @GetMapping("movies")
    public String movies() {

        return "admin/movies";
    }

    // 음악 리스트를 모두 보여주는 화면
    @GetMapping("music")
    public String music(){
        return "admin/music";
    }

    // 도서 리스트를 모두 보여주는 화면
    @GetMapping("books")
    public String books(){
        return "admin/books";
    }

    // 영화 추가 화면
    @GetMapping("movies/add")
    public String addMovie(MovieAddRequest movieAddRequest, Model model){
        return "admin/movies-add";
    }

}
