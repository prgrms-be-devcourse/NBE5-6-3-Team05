package com.grepp.moodlink.app.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.moodlink.app.model.movie.MovieService;
import com.grepp.moodlink.app.model.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.music.MusicService;
import com.grepp.moodlink.app.model.music.dto.MusicDto;
import com.grepp.moodlink.app.model.music.entity.Music;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicImportController {
    @Autowired
    private final MusicService musicService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/upload")
    public ResponseEntity<String> importMovies(@RequestPart("file") MultipartFile file)
        throws IOException {
        List<MusicDto> dtos = objectMapper.readValue(
            file.getInputStream(),
            new TypeReference<List<MusicDto>>() {
            }
        );
        musicService.saveMusic(dtos);
        return ResponseEntity.ok("Music saved!");
    }
}
