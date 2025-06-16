package com.grepp.moodlink.app.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.movie.MovieService;
import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.music.MusicService;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import java.io.IOException;
import java.util.List;

import com.grepp.moodlink.app.model.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class ImportController {

    @Autowired
    private final MovieService movieService;
    @Autowired
    private final MusicService musicService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookService bookService;
    @Autowired
    private KeywordService keywordService;

    @PostMapping("/movies")
    public ResponseEntity<String> importMovies(@RequestPart("file") MultipartFile file)
        throws IOException {
        List<MovieDto> dtos = objectMapper.readValue(
            file.getInputStream(),
            new TypeReference<List<MovieDto>>() {
            }
        );
        movieService.saveMovies(dtos);
        return ResponseEntity.ok("Movies saved!");
    }

    @PostMapping("/music")
    public ResponseEntity<String> importMusic(@RequestPart("file") MultipartFile file)
        throws IOException {
        List<MusicDto> dtos = objectMapper.readValue(
            file.getInputStream(),
            new TypeReference<List<MusicDto>>() {
            }
        );
        musicService.saveMusic(dtos);
        return ResponseEntity.ok("Music saved!");
    }

    @PostMapping("/books")
    public ResponseEntity<String> importBook(@RequestPart("file") MultipartFile file)
        throws IOException {
        List<BookDto> dtos = objectMapper.readValue(
            file.getInputStream(),
            new TypeReference<List<BookDto>>() {
            }
        );
        bookService.saveMusic(dtos);
        return ResponseEntity.ok("Book saved!");
    }

    // 키워드 등록
    @PostMapping("/keyword")
    public ResponseEntity<String> importKeyword() throws Exception {
        keywordService.importKeywordsFromFile();

        return ResponseEntity.ok("Keyword Upload");
    }
}