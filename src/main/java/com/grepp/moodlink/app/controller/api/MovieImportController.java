package com.grepp.moodlink.app.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.moodlink.app.model.movie.MovieService;
import com.grepp.moodlink.app.model.movie.dto.MovieDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieImportController {
    @Autowired
    private final MovieService movieService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/upload")
    public ResponseEntity<String> importMovies(@RequestPart("file") MultipartFile file) throws IOException {
        List<MovieDto> dtos = objectMapper.readValue(
                file.getInputStream(),
                new TypeReference<List<MovieDto>>() {}
        );
        movieService.saveMovies(dtos);
        return ResponseEntity.ok("Movies saved!");
    }
}
