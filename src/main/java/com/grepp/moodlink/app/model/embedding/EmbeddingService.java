package com.grepp.moodlink.app.model.embedding;

import com.grepp.moodlink.app.model.data.book.BookRepository;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.movie.MovieRepository;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.music.MusicRepository;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.keyword.KeywordRepository;
import com.grepp.moodlink.app.model.keyword.entity.KeywordSelection;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {
    private final MovieRepository movieRepository;
    private final BookRepository bookRepository;
    private final MusicRepository musicRepository;
    private final EmbeddingModel embeddingModel;
    private final KeywordRepository keywordRepository;

    @Transactional
    @Async
    public void generateEmbeddingsMovie() {
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

    @Transactional
    @Async
    public void generateEmbeddingsBook() {
        List<Book> books = bookRepository.findByEmbeddingIsNull();
        for (Book book : books) {
            String text = book.getDescription();
            float[] floatEmbedding = embeddingModel.embed(text);

            byte[] byteEmbedding = toByteArray(floatEmbedding);
            book.setEmbedding(byteEmbedding);

            bookRepository.save(book);
        }
    }

    @Transactional
    @Async
    public void generateEmbeddingsMusic() {
        List<Music> musics = musicRepository.findByEmbeddingIsNull();
        for (Music music : musics) {
            String text = music.getLyrics();
            float[] floatEmbedding = embeddingModel.embed(text);

            byte[] byteEmbedding = toByteArray(floatEmbedding);
            music.setEmbedding(byteEmbedding);

            musicRepository.save(music);
        }
    }

    private byte[] toByteArray(float[] floats) {
        ByteBuffer buffer = ByteBuffer.allocate(floats.length * Float.BYTES);
        for (float f : floats) {
            buffer.putFloat(f);
        }
        return buffer.array();
    }

    private float[] toFloatArray(byte[] bytes) {
        FloatBuffer buffer = ByteBuffer.wrap(bytes).asFloatBuffer();
        float[] floats = new float[buffer.remaining()];
        buffer.get(floats);
        return floats;
    }

    public void generateEmbeddingKeyword(String userId, String keywords) {
        KeywordSelection keywordSelection = keywordRepository.findByUserId(userId);
        float[] floatEmbedding = embeddingModel.embed(keywords);
        byte[] byteEmbedding = toByteArray(floatEmbedding);
        keywordSelection.setEmbedding(byteEmbedding);

        keywordRepository.save(keywordSelection);
    }

    public List<Movie> cosineComputeMovie(String userId) {
        KeywordSelection keywordSelection = keywordRepository.findByUserId(userId);
        byte[] byteEmbedding = keywordSelection.getEmbedding();
        float[] floatEmbedding = toFloatArray(byteEmbedding);
        List<Movie> movies = movieRepository.findAll();
        PriorityQueue<Map.Entry<String, Float>> pq = new PriorityQueue<>((a, b) -> Float.compare(b.getValue(), a.getValue()));

        for (Movie movie : movies) {
            byte[] byteMovie = movie.getEmbedding();
            float[] floatMovie = toFloatArray(byteMovie);
            float cosingSimilarity = CosineSimilarity.compute(floatEmbedding, floatMovie);
            Map.Entry<String, Float> entry = new AbstractMap.SimpleEntry<>(movie.getId(), cosingSimilarity);

            pq.add(entry);
        }

        List<String> recommendedMovieIds = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < 5) {
            Map.Entry<String, Float> entry = pq.poll();
            recommendedMovieIds.add(entry.getKey());
            count++;
        }

        recommendedMovieIds.forEach(System.out::println);
        return movieRepository.findAllById(recommendedMovieIds);
    }
}

