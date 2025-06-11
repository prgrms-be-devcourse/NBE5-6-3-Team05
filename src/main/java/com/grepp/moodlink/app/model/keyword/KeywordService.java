package com.grepp.moodlink.app.model.keyword;

import com.grepp.moodlink.app.model.keyword.entity.KeywordSelection;
import dev.langchain4j.model.embedding.EmbeddingModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final EmbeddingModel embeddingModel;

    public String findReason(String keywords) {
        Optional<KeywordSelection> keywordSelection = keywordRepository.findByKeywords(keywords);
        return keywordSelection.get().getReason();
    }

    public void importKeywordsFromFile() throws Exception {
        ClassPathResource resource = new ClassPathResource("keyword_combinations.txt");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String keywords = line.trim();
                if (!keywords.isEmpty() && !keywordRepository.existsByKeywords(keywords)) {
                    KeywordSelection entity = new KeywordSelection();
                    entity.setKeywords(keywords);
                    entity.setEmbedding(generateEmbeddingKeyword(keywords));
                    entity.setReason(null);    // 추천 이유는 추후 배치에서 생성
                    keywordRepository.save(entity);
                }
            }
        }
    }

    public void generateKeywordSelection(String keywords) {
        KeywordSelection keywordSelection = new KeywordSelection();
        keywordSelection.setKeywords(keywords);
        keywordSelection.setEmbedding(generateEmbeddingKeyword(keywords));
        keywordRepository.save(keywordSelection);
    }

    private byte[] generateEmbeddingKeyword(String keywords) {
        Optional<KeywordSelection> keywordSelection = keywordRepository.findByKeywords(keywords);
        float[] floatEmbedding = embeddingModel.embed(keywords).content().vector();
        return toByteArray(floatEmbedding);
    }

    private byte[] toByteArray(float[] floats) {
        ByteBuffer buffer = ByteBuffer.allocate(floats.length * Float.BYTES);
        for (float f : floats) {
            buffer.putFloat(f);
        }
        return buffer.array();
    }

    public boolean exist(String keywords) {
        Optional<KeywordSelection> keywordSelection = keywordRepository.findByKeywords(keywords);
        return keywordSelection.isPresent();
    }

}
