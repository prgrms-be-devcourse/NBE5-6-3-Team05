package com.grepp.moodlink.app.controller.api.details;

import com.grepp.moodlink.app.model.details.DetailsService;
import com.grepp.moodlink.app.model.details.dto.BookDetailsDto;
import com.grepp.moodlink.app.model.details.dto.MovieDetailsDto;
import com.grepp.moodlink.app.model.details.dto.SongDetailsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/details")
public class DetailsApiController {
    //TODO: 각 컨텐츠의 상세정보 view로 넘기기 (GetMapping)

    private final DetailsService detailsService;

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDetailsDto> getBookDetails(@PathVariable String id) {
        BookDetailsDto dto = detailsService.getBookDetails(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/song/{id}")
    public ResponseEntity<SongDetailsDto> getSongDetails(@PathVariable String id) {
        SongDetailsDto dto = detailsService.getSongDetails(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<MovieDetailsDto> getMovieDetails(@PathVariable String id) {
        MovieDetailsDto dto = detailsService.getMovieDetails(id);
        return ResponseEntity.ok(dto);
    }
}
