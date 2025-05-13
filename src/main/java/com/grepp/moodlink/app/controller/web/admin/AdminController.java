package com.grepp.moodlink.app.controller.web.admin;

import com.grepp.moodlink.app.controller.web.admin.payload.BookAddRequest;
import com.grepp.moodlink.app.controller.web.admin.payload.MovieAddRequest;
import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.book.code.Genre;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final BookService bookService;

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
    public String books(Model model){
        List<BookDto> books = bookService.findAll();

        model.addAttribute("books",books);

        return "admin/books";
    }

    // 영화 추가 화면
    @GetMapping("movies/add")
    public String addMovie(MovieAddRequest movieAddRequest, Model model){
        return "admin/movies-add";
    }

    // 도서 추가 화면
    @GetMapping("books/add")
    public String addBooks(BookAddRequest bookAddRequest, Model model){
        model.addAttribute("genres", Genre.values());
        return "admin/books-add";
    }

    // 도서 추가 화면
    @PostMapping("books/add")
    public String addBooks(@Valid BookAddRequest bookAddRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "admin/books-add";
        }

        try{
            bookService.addBook(bookAddRequest.getImage(), bookAddRequest.toDto());
        }catch(CommonException e){
            log.info(e.code().message());
            redirectAttributes.addFlashAttribute("errorMessage", e.code().message());
            return "redirect:/admin/books/add";
        }

        return "redirect:/admin/books";
    }
}
