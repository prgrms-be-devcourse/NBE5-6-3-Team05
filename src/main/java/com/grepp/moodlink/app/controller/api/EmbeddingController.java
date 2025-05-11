package com.grepp.moodlink.app.controller.api;

import com.grepp.moodlink.app.model.embedding.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/embedding")
@RequiredArgsConstructor
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmbeddings() {
        embeddingService.generateEmbeddings();
        return ResponseEntity.ok("임베딩 생성 작업이 시작되었습니다.");
    }
}
