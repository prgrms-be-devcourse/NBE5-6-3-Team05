package com.grepp.moodlink.app.controller.api;

import com.grepp.moodlink.app.model.llm.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/embedding")
@RequiredArgsConstructor
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    @PostMapping("/movie")
    public ResponseEntity<String> generateEmbeddingsMovie() {
        embeddingService.generateEmbeddingsMovie();
        return ResponseEntity.ok("영화 임베딩 생성 작업이 시작되었습니다.");
    }

    @PostMapping("/book")
    public ResponseEntity<String> generateEmbeddingsBook() {
        embeddingService.generateEmbeddingsBook();
        return ResponseEntity.ok("도서 임베딩 생성 작업이 시작되었습니다.");
    }
    @PostMapping("/music")
    public ResponseEntity<String> generateEmbeddingsMusic() {
        embeddingService.generateEmbeddingsMusic();
        return ResponseEntity.ok("음악 임베딩 생성 작업이 시작되었습니다.");
    }
}
