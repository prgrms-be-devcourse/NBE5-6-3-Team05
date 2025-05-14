package com.grepp.moodlink.app.controller.web.admin;

import com.grepp.moodlink.app.controller.web.admin.payload.BookAddRequest;
import com.grepp.moodlink.app.controller.web.admin.payload.BookModifyRequest;
import com.grepp.moodlink.app.controller.web.admin.payload.MovieAddRequest;
import com.grepp.moodlink.app.controller.web.admin.payload.MovieModifyRequest;
import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.book.code.Genre;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.movie.MovieService;
import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.payload.PageParam;
import com.grepp.moodlink.infra.response.PageResponse;
import com.grepp.moodlink.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final MovieService movieService;

    // 영화 리스트를 모두 보여주는 화면
    // 관리자 페이지의 기본화면
    @GetMapping("movies")
    public String movies(Model model, @Valid PageParam param, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(param.getPage() - 1, param.getSize());
        Page<MovieInfoDto> page = movieService.findPaged(pageable);

        if(param.getPage() != 1 && page.getContent().isEmpty()){
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        PageResponse<MovieInfoDto> response = new PageResponse<>("/admin/movies", page, 10);
        model.addAttribute("page", response);

        return "admin/movies";
    }

    // 음악 리스트를 보여주는 화면
    @GetMapping("music")
    public String music(){
        return "admin/music";
    }

    // 도서 리스트를 보여주는 화면
    @GetMapping("books")
    public String books(Model model, @Valid PageParam param, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(param.getPage() - 1, param.getSize());
        Page<BookDto> page = bookService.findPaged(pageable);

        if(param.getPage() != 1 && page.getContent().isEmpty()){
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        PageResponse<BookDto> response = new PageResponse<>("/admin/books", page, 10);
        model.addAttribute("page", response);
        return "admin/books";
    }

    // 영화 추가 화면
    @GetMapping("movies/add")
    public String addMovie(MovieAddRequest movieAddRequest, Model model){
        model.addAttribute("genreList", movieService.findAllGenre());
        return "admin/movies-add";
    }

    // 영화 추가 화면
    @PostMapping("movies/add")
    public String addBooks(@Valid MovieAddRequest movieAddRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/admin/books/add";
        }

        try{
            movieService.addMovie(movieAddRequest.getThumbnail(), movieAddRequest.toDto());
        }catch(CommonException e){
            log.info(e.code().message());
            redirectAttributes.addFlashAttribute("errorMessage", e.code().message());
            return "redirect:/admin/books/add";
        }

        return "redirect:/admin/books";
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
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/admin/books/add";
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

    // 영화 수정 화면
    @GetMapping("movies/modify/{id}")
    public String modifyMovies(MovieModifyRequest movieModifyRequest, @PathVariable String id, Model model){

        // title releasedDate 기록 가져오기
        MovieInfoDto movie = movieService.findById(id);
        model.addAttribute("movie",movie);


        // movieModifyRequest 기존에 저장된 데이터 값 넣어두기
        movieModifyRequest.setDescription(movie.getDescription());
        String selectedGenre = "";
        for(com.grepp.moodlink.app.model.data.movie.entity.Genre genre : movie.getGenres()){
            selectedGenre=selectedGenre.concat(genre.getId()+",");
        }
        selectedGenre = selectedGenre.substring(0, selectedGenre.length()-1);
        log.info(selectedGenre);
        model.addAttribute("selectedGenre",selectedGenre);
        // 이미지는 파일 형식이 달라서 더 고민해보기



        // 미리 값을 저장해둔 request 넘기기
        model.addAttribute("movieModifyRequest",movieModifyRequest);
        // 장르 데이터 넘기기
        model.addAttribute("genreList", movieService.findAllGenre());

        return "admin/movies-modify";
    }

    // 영화 수정 화면
    @PostMapping("movies/modify/{id}")
    public String modifyMovies(
        @PathVariable String id,
        @Valid MovieModifyRequest movieModifyRequest,
        BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/admin/movies/modify/{id}";
        }

        // Service로 넘길 dto 만들기
        MovieInfoDto dto = movieModifyRequest.toDto();
        dto.setId(id);
        log.info("{}", dto);

        movieService.updateMovie(movieModifyRequest.getImage(), dto);

        return "redirect:/admin/movies";
    }

    // 도서 수정 화면
    @GetMapping("books/modify/{isbn}")
    public String modifyBooks(BookModifyRequest bookModifyRequest, @PathVariable String isbn, Model model){

        // title author 기록 가져오기
        BookDto book = bookService.findByIsbn(isbn);

        model.addAttribute("book",book);

        // bookModifyRequest에 기존에 저장된 데이터 값 넣어두기
        bookModifyRequest.setDescription(book.getDescription());
        bookModifyRequest.setPublisher(book.getPublisher());
        bookModifyRequest.setGenre(book.getGenre());
        bookModifyRequest.setPublishedDate(book.getPublishedDate());
        // 이미지는 파일 형식이 달라서 더 고민해보기


        // 미리 값을 저장해둔 request 넘기기
        model.addAttribute("bookModifyRequest",bookModifyRequest);
        // 장르 데이터 넘기기
        model.addAttribute("genres", Genre.values());
        return "admin/books-modify";
    }

    // 도서 수정 화면
    @PostMapping("books/modify/{isbn}")
    public String modifyBooks(
        @PathVariable String isbn,
        @Valid BookModifyRequest bookModifyRequest,
        BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/admin/books/modify/{isbn}";
        }

        // Service로 넘길 dto 만들기
        BookDto dto = bookModifyRequest.toDto();
        dto.setIsbn(isbn);
        log.info("{}", dto);

        bookService.updateBook(bookModifyRequest.getImage(), dto);

        return "redirect:/admin/books";
    }
}
