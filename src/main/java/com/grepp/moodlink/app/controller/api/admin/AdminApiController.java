package com.grepp.moodlink.app.controller.api.admin;

import com.grepp.moodlink.app.model.admin.book.AdminBookService;
import com.grepp.moodlink.app.model.admin.movie.AdminMovieService;
import com.grepp.moodlink.app.model.admin.music.AdminMusicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminApiController {

    private final AdminBookService bookService;
    private final AdminMovieService movieService;
    private final AdminMusicService musicService;

    // 영화 삭제
    @DeleteMapping("movies/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }

    // 영화 삭제
    @DeleteMapping("music/delete/{id}")
    public ResponseEntity<String> deleteMusic(@PathVariable String id) {
        musicService.deleteMusic(id);
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }


    // 도서 삭제
    @DeleteMapping("books/delete/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }



}
