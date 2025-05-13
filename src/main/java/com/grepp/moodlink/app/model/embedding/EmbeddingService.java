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
            String text = "영화 제목 : " + movie.getTitle()
                    + "영화 설명" + movie.getDescription();
            float[] floatEmbedding = embeddingModel.embed(text);

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
            String text = "도서 제목 : " + book.getTitle()
                    + "도서 설명 :" + book.getDescription();
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
            String text = "노래 제목 : " + music.getTitle()
                    + "노래 가사 : " + music.getLyrics();
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

    public List<Movie> cosineComputeMovie(String genre, String userId) {
        KeywordSelection keywordSelection = keywordRepository.findByUserId(userId);
        byte[] byteEmbedding = keywordSelection.getEmbedding();
        float[] floatEmbedding = toFloatArray(byteEmbedding);
        List<Movie> movies = null;
        if (genre.isBlank()){
             movies = movieRepository.findAll();
        }else {
            movies = movieRepository.findByGenreName(genre);
        }

        PriorityQueue<Map.Entry<String, Float>> pq = new PriorityQueue<>((a, b) -> Float.compare(b.getValue(), a.getValue()));
        for (Movie movie : movies) {
            byte[] byteMovie = movie.getEmbedding();
            float[] floatMovie = toFloatArray(byteMovie);
            float distance = CosineSimilarity.compute(floatEmbedding, floatMovie);
            pq.add(new AbstractMap.SimpleEntry<>(movie.getId(), distance));
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

    public Object cosineComputeBook(String userId) {
        KeywordSelection keywordSelection = keywordRepository.findByUserId(userId);
        byte[] byteEmbedding = keywordSelection.getEmbedding();
        float[] floatEmbedding = toFloatArray(byteEmbedding);
        List<Book> books = bookRepository.findAll();
        PriorityQueue<Map.Entry<String, Float>> pq = new PriorityQueue<>((a, b) -> Float.compare(b.getValue(), a.getValue()));

        for (Book book : books) {
            byte[] byteBook = book.getEmbedding();
            float[] floatBook = toFloatArray(byteBook);
            float distance = PearsonCorrelation.compute(floatEmbedding, floatBook);
            pq.add(new AbstractMap.SimpleEntry<>(book.getIsbn(), distance));
        }

        List<String> recommendedBookIds = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < 5) {
            Map.Entry<String, Float> entry = pq.poll();
            recommendedBookIds.add(entry.getKey());
            count++;
        }

        recommendedBookIds.forEach(System.out::println);
        return bookRepository.findAllById(recommendedBookIds);
    }

    public Object cosineComputeMusic(String userId) {
        KeywordSelection keywordSelection = keywordRepository.findByUserId(userId);
        byte[] byteEmbedding = keywordSelection.getEmbedding();
        float[] floatEmbedding = toFloatArray(byteEmbedding);
        List<Music> musics = musicRepository.findAll();
        PriorityQueue<Map.Entry<String, Float>> pq = new PriorityQueue<>((a, b) -> Float.compare(b.getValue(), a.getValue()));

        for (Music music : musics) {
            byte[] byteMusic = music.getEmbedding();
            float[] floatMusic = toFloatArray(byteMusic);
            float cosingSimilarity = CosineSimilarity.compute(floatEmbedding, floatMusic);
            Map.Entry<String, Float> entry = new AbstractMap.SimpleEntry<>(music.getId(), cosingSimilarity);

            pq.add(entry);
        }

        List<String> recommendedMusicIds = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < 5) {
            Map.Entry<String, Float> entry = pq.poll();
            recommendedMusicIds.add(entry.getKey());
            count++;
        }

        recommendedMusicIds.forEach(System.out::println);
        return musicRepository.findAllById(recommendedMusicIds);
    }

    public static Set<Integer> toBinarySet(float[] vector) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] > 0.5) { // 임계값 설정
                set.add(i);
            }
        }
        return set;
    }
}

