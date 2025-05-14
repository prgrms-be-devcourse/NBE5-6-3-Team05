package com.grepp.moodlink.app.controller.api.admin;

import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.movie.MovieRepository;
import com.grepp.moodlink.app.model.data.movie.MovieService;
import com.grepp.moodlink.infra.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminApiController {

    private final BookService bookService;
    private final MovieService movieService;

    // 영화 삭제
    @DeleteMapping("movies/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable String id) {
        log.info("삭제합니다!!");
        movieService.deleteMovie(id);
        log.info("삭제완료~~~~~~~~~~~~");
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }


    // 도서 삭제
    @DeleteMapping("books/delete/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        log.info("삭제합니다!!");
        bookService.deleteBook(isbn);
        log.info("삭제완료~~~~~~~~~~~~");
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }

}
