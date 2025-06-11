package com.grepp.moodlink.app.controller.api.search;

import com.grepp.moodlink.app.model.home.SearchService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trailer")
@RequiredArgsConstructor
public class TrailerController {

    private final SearchService searchService;

    @GetMapping("/movie")
    public ResponseEntity<Map<String, String>> getMovieTrailer(@RequestParam String title) {
        try {
            String movieVideoId = searchService.searchYoutubeMovieTrailer(title);
            Map<String, String> result = new HashMap<>();
            result.put("movieVideoId", movieVideoId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to fetch trailer"));
        }
    }

    @GetMapping("/music")
    public ResponseEntity<Map<String, String>> getMusicTrailer(@RequestParam String title) {
        try {
            String musicVideoId = searchService.searchYoutubeMusicTrailer(title);
            Map<String, String> result = new HashMap<>();
            result.put("musicVideoId", musicVideoId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to fetch trailer"));
        }
    }
}