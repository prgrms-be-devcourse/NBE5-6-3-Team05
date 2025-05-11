package com.grepp.moodlink.app.model.embedding;

import com.grepp.moodlink.app.model.movie.MovieRepository;
import com.grepp.moodlink.app.model.movie.entity.Movie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {
    private final MovieRepository movieRepository;
    private final EmbeddingModel embeddingModel;

    @Transactional
    @Async
    public void generateEmbeddings() {
        List<Movie> movies = movieRepository.findByEmbeddingIsNull();
        for (Movie movie : movies) {
            String text = movie.getDescription();
            float[] floatEmbedding = embeddingModel.embed(text);

            // float[] → byte[] 변환
            byte[] byteEmbedding = toByteArray(floatEmbedding);
            movie.setEmbedding(byteEmbedding);

            movieRepository.save(movie);
        }
    }

    // float[] → byte[] 변환 메서드
    private byte[] toByteArray(float[] floats) {
        ByteBuffer buffer = ByteBuffer.allocate(floats.length * Float.BYTES);
        for (float f : floats) {
            buffer.putFloat(f);
        }
        return buffer.array();
    }

    // byte[] → float[] 변환 메서드 (필요시)
    private float[] toFloatArray(byte[] bytes) {
        FloatBuffer buffer = ByteBuffer.wrap(bytes).asFloatBuffer();
        float[] floats = new float[buffer.remaining()];
        buffer.get(floats);
        return floats;
    }
}

