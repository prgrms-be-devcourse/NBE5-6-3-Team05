package com.grepp.moodlink.app.controller.api.details;

import com.grepp.moodlink.app.controller.api.details.payload.BookDetailResponse;
import com.grepp.moodlink.app.controller.api.details.payload.MovieDetailResponse;
import com.grepp.moodlink.app.controller.api.details.payload.MusicDetailResponse;
import com.grepp.moodlink.app.model.details.DetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/details")
public class DetailsApiController {

    private final DetailsService detailsService;

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDetailResponse> getBookDetails(@PathVariable String id,
        Authentication authentication) {
        String userId;
        userId = "";
        if (authentication != null) {
            userId = authentication.getName();
        }

        BookDetailResponse dto = detailsService.getBookDetails(userId, id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/musics/{id}")
    public ResponseEntity<MusicDetailResponse> getSongDetails(@PathVariable String id,
        Authentication authentication) {
        String userId;
        userId = "";
        if (authentication != null) {
            userId = authentication.getName();
        }
        MusicDetailResponse dto = detailsService.getSongDetails(userId, id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDetailResponse> getMovieDetails(@PathVariable String id,
        Authentication authentication) {
        String userId;
        userId = "";
        if (authentication != null) {
            userId = authentication.getName();
        }
        MovieDetailResponse dto = detailsService.getMovieDetails(userId, id);
        return ResponseEntity.ok(dto);
    }


}
