package com.grepp.moodlink.app.controller.web.admin;

import com.grepp.moodlink.app.controller.web.admin.payload.BookAddRequest;
import com.grepp.moodlink.app.controller.web.admin.payload.BookModifyRequest;
import com.grepp.moodlink.app.controller.web.admin.payload.MovieAddRequest;
import com.grepp.moodlink.app.controller.web.admin.payload.MovieModifyRequest;
import com.grepp.moodlink.app.controller.web.admin.payload.MusicAddRequest;
import com.grepp.moodlink.app.controller.web.admin.payload.MusicModifyRequest;
import com.grepp.moodlink.app.model.admin.book.AdminBookService;
import com.grepp.moodlink.app.model.admin.movie.AdminMovieService;
import com.grepp.moodlink.app.model.admin.music.AdminMusicService;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.dto.BookGenreDto;
import com.grepp.moodlink.app.model.data.movie.dto.GenreDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.dto.MusicGenreDto;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.payload.PageParam;
import com.grepp.moodlink.infra.response.PageResponse;
import com.grepp.moodlink.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminBookService bookService;
    private final AdminMovieService movieService;
    private final AdminMusicService musicService;

    // 영화 리스트를 모두 보여주는 화면
    // 관리자 페이지의 기본화면
    @GetMapping("movies")
    public String movies(Model model, @Valid PageParam param, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(param.getPage() - 1, param.getSize());
        Page<MovieInfoDto> page = movieService.findPaged(pageable);

        if (param.getPage() != 1 && page.getContent().isEmpty()) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        PageResponse<MovieInfoDto> response = new PageResponse<>("/admin/movies", page, 10);
        model.addAttribute("page", response);

        return "admin/contents/movies";
    }

    // 음악 리스트를 보여주는 화면
    @GetMapping("music")
    public String music(Model model, @Valid PageParam param, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(param.getPage() - 1, param.getSize());
        Page<MusicDto> page = musicService.findPaged(pageable);

        if (param.getPage() != 1 && page.getContent().isEmpty()) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        PageResponse<MusicDto> response = new PageResponse<>("/admin/music", page, 10);
        model.addAttribute("page", response);
        return "admin/contents/music";
    }

    // 도서 리스트를 보여주는 화면
    @GetMapping("books")
    public String books(Model model, @Valid PageParam param, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(param.getPage() - 1, param.getSize());
        Page<BookDto> page = bookService.findPaged(pageable);

        if (param.getPage() != 1 && page.getContent().isEmpty()) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        PageResponse<BookDto> response = new PageResponse<>("/admin/books", page, 10);
        model.addAttribute("page", response);
        return "admin/contents/books";
    }

    // 영화 추가 화면
    @GetMapping("movies/add")
    public String addMovie(@ModelAttribute("movieAddRequest") MovieAddRequest movieAddRequest,
        Model model) {
        model.addAttribute("genreList", movieService.findAllGenre());
        return "admin/contents/movies-add";
    }


    // 영화 추가 화면
    @PostMapping("movies/add")
    public String addMovie(@Valid MovieAddRequest movieAddRequest,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes) {
        // redirect 시에 오류 메시지를 보내기 위해
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError err : bindingResult.getFieldErrors()) {
                fieldErrors.put(err.getField(), err.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("movieAddRequest", movieAddRequest);
            return "redirect:/admin/movies/add";
        }

        try {
            movieService.addMovie(movieAddRequest.getThumbnail(), movieAddRequest.toDto());
        } catch (CommonException e) {
            log.info(e.code().message());
            Map<String, String> fieldErrors = new HashMap<>();
            fieldErrors.put("title",e.code().message());
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            return "redirect:/admin/movies/add";
        }

        return "redirect:/admin/movies";
    }


    // 음악 추가 화면
    @GetMapping("music/add")
    public String addMusic(@ModelAttribute("musicAddRequest") MusicAddRequest musicAddRequest,
        Model model) {
        model.addAttribute("genres", musicService.findAllGenreName());
        return "admin/contents/music-add";
    }

    // 음악 추가 화면
    @PostMapping("music/add")
    public String addMusic(@Valid MusicAddRequest musicAddRequest, BindingResult bindingResult,
        RedirectAttributes redirectAttributes) {
        // redirect 시에 오류 메시지를 보내기 위해
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError err : bindingResult.getFieldErrors()) {
                fieldErrors.put(err.getField(), err.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("musicAddRequest", musicAddRequest);
            return "redirect:/admin/music/add";
        }

        try {
            musicService.addMusic(musicAddRequest.getThumbnail(), musicAddRequest.toDto());
        } catch (CommonException e) {
            log.info(e.code().message());
            Map<String, String> fieldErrors = new HashMap<>();
            fieldErrors.put("title",e.code().message());
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            return "redirect:/admin/music/add";
        }

        return "redirect:/admin/music";
    }

    // 도서 추가 화면
    @GetMapping("books/add")
    public String addBooks(@ModelAttribute("bookAddRequest") BookAddRequest bookAddRequest,
        Model model) {
        model.addAttribute("genres", bookService.findAllGenreName());
        return "admin/contents/books-add";
    }

    // 도서 추가 화면
    @PostMapping("books/add")
    public String addBooks(@Valid BookAddRequest bookAddRequest, BindingResult bindingResult,
        RedirectAttributes redirectAttributes) {
        // redirect 시에 오류 메시지를 보내기 위해
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError err : bindingResult.getFieldErrors()) {
                fieldErrors.put(err.getField(), err.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("bookAddRequest", bookAddRequest);
            return "redirect:/admin/books/add";
        }

        try {
            bookService.addBook(bookAddRequest.getImage(), bookAddRequest.toDto());
        } catch (CommonException e) {
            log.info(e.code().message());
            Map<String, String> fieldErrors = new HashMap<>();
            fieldErrors.put("title",e.code().message());
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            return "redirect:/admin/books/add";
        }

        return "redirect:/admin/books";
    }

    // 영화 수정 화면
    @GetMapping("movies/modify/{id}")
    public String modifyMovies(MovieModifyRequest movieModifyRequest, @PathVariable String id,
        Model model, RedirectAttributes redirectAttributes) {
        try{

        // title releasedDate 기록 가져오기
        MovieInfoDto movie = movieService.findById(id);
        model.addAttribute("movie", movie);

        // movieModifyRequest 기존에 저장된 데이터 값 넣어두기
        movieModifyRequest.setDescription(movie.getDescription());
        String selectedGenre = "";
        for (Genre genre : movie.getGenres()) {
            selectedGenre = selectedGenre.concat(genre.getId() + ",");
        }
        selectedGenre = selectedGenre.substring(0, selectedGenre.length() - 1);
        model.addAttribute("selectedGenre", selectedGenre);

        // 미리 값을 저장해둔 request 넘기기
        model.addAttribute("movieModifyRequest", movieModifyRequest);
        // 장르 데이터 넘기기
        model.addAttribute("genreList", movieService.findAllGenre());
        }catch(CommonException e){
            redirectAttributes.addFlashAttribute("error", "더이상 존재하지 않는 데이터입니다.");
            return "redirect:/admin/movies";
        }

        return "admin/contents/movies-modify";
    }

    // 영화 수정 화면
    @PostMapping("movies/modify/{id}")
    public String modifyMovies(
        @PathVariable String id,
        @Valid MovieModifyRequest movieModifyRequest,
        BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // redirect 시에 오류 메시지를 보내기 위해
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError err : bindingResult.getFieldErrors()) {
                fieldErrors.put(err.getField(), err.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            return "redirect:/admin/movies/modify/{id}";
        }

        // Service로 넘길 dto 만들기
        MovieInfoDto dto = movieModifyRequest.toDto();
        dto.setId(id);
        log.info("{}", movieModifyRequest.getDescription());

        movieService.updateMovie(movieModifyRequest.getThumbnail(), dto);

        return "redirect:/admin/movies";
    }


    // 도서 수정 화면
    @GetMapping("books/modify/{isbn}")
    public String modifyBooks(BookModifyRequest bookModifyRequest, @PathVariable String isbn,
        Model model,RedirectAttributes redirectAttributes) {

        try{
        // title author 기록 가져오기
        BookDto book = bookService.findByIsbn(isbn);

        model.addAttribute("book", book);

        // bookModifyRequest에 기존에 저장된 데이터 값 넣어두기
        bookModifyRequest.setDescription(book.getDescription());
        bookModifyRequest.setPublisher(book.getPublisher());
        bookModifyRequest.setGenre(book.getGenre());
        bookModifyRequest.setPublishedDate(book.getPublishedDate());

        // 미리 값을 저장해둔 request 넘기기
        model.addAttribute("bookModifyRequest", bookModifyRequest);
        // 장르 데이터 넘기기
        model.addAttribute("genres", bookService.findAllGenreName());
        }catch(CommonException e){
            redirectAttributes.addFlashAttribute("error", "더이상 존재하지 않는 데이터입니다.");
            return "redirect:/admin/books";
        }
        return "admin/contents/books-modify";
    }

    // 도서 수정 화면
    @PostMapping("books/modify/{isbn}")
    public String modifyBooks(
        @PathVariable String isbn,
        @Valid BookModifyRequest bookModifyRequest,
        BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // redirect 시에 오류 메시지를 보내기 위해
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError err : bindingResult.getFieldErrors()) {
                fieldErrors.put(err.getField(), err.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            return "redirect:/admin/books/modify/{isbn}";
        }

        // Service로 넘길 dto 만들기
        BookDto dto = bookModifyRequest.toDto();
        dto.setIsbn(isbn);
        log.info("{}", dto);
        log.info("{}", bookModifyRequest.getImage().isEmpty());
        log.info("{}", bookModifyRequest);

        bookService.updateBook(bookModifyRequest.getImage(), dto);

        return "redirect:/admin/books";
    }

    // 음악 수정 화면
    @GetMapping("music/modify/{id}")
    public String modifyMusic(MusicModifyRequest musicModifyRequest, @PathVariable String id,
        Model model,RedirectAttributes redirectAttributes) {
        try{

        // title author 기록 가져오기
        MusicDto music = musicService.findById(id);

        model.addAttribute("music", music);

        // bookModifyRequest에 기존에 저장된 데이터 값 넣어두기
        musicModifyRequest.setReleaseDate(music.getReleaseDate());
        musicModifyRequest.setGenre(music.getGenre());
        musicModifyRequest.setDescription(music.getDescription());
        musicModifyRequest.setLyrics(music.getLyrics());
        // 이미지는 파일 형식이 달라서 더 고민해보기

        // 미리 값을 저장해둔 request 넘기기
        model.addAttribute("musicModifyRequest", musicModifyRequest);
        // 장르 데이터 넘기기
        model.addAttribute("genres", musicService.findAllGenreName());
        }catch(CommonException e){
            redirectAttributes.addFlashAttribute("error", "더이상 존재하지 않는 데이터입니다.");
            return "redirect:/admin/music";
        }
        return "admin/contents/music-modify";
    }

    // 음악 수정 화면
    @PostMapping("music/modify/{id}")
    public String modifyMusic(
        @PathVariable String id,
        @Valid MusicModifyRequest musicModifyRequest,
        BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // redirect 시에 오류 메시지를 보내기 위해
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError err : bindingResult.getFieldErrors()) {
                fieldErrors.put(err.getField(), err.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            return "redirect:/admin/music/modify/{id}";
        }

        // Service로 넘길 dto 만들기
        MusicDto dto = musicModifyRequest.toDto();
        dto.setId(id);
        log.info("{}", dto);

        musicService.updateMusic(musicModifyRequest.getThumbnail(), dto);

        return "redirect:/admin/music";
    }

    // 장르 화면
    @GetMapping("genres")
    public String genres(Model model){

        // 미리 모든 정보 전달
        List<GenreDto> movieGenres = movieService.findAllGenre();
        List<MusicGenreDto> musicGenres = musicService.findAllGenre();
        List<BookGenreDto> bookGenres = bookService.findAllGenre();
        model.addAttribute("movieGenres", movieGenres);
        model.addAttribute("musicGenres", musicGenres);
        model.addAttribute("bookGenres", bookGenres);

        // 해당 장르를 사용하는 컨텐츠가 얼마나 있는지 count하여 맵에 장르 이름과 함께 저장
        Map<String, Long> genreCount = new HashMap<>();
        for(GenreDto genre : movieGenres){
            genreCount.put(genre.getName(), movieService.countMoviesByGenre(genre.getId()));
        }
        for(MusicGenreDto genre : musicGenres){
            genreCount.put(genre.getName(), musicService.countMusicByGenre(genre.getId()));
        }
        for(BookGenreDto genre: bookGenres){
            genreCount.put(genre.getName(), bookService.countBooksByGenre(genre.getId()));
        }

        model.addAttribute("genreCount", genreCount);
        return "admin/contents/genres";
    }

}
