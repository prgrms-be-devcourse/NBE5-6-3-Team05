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
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import javax.naming.ServiceUnavailableException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {
    private final MovieRepository movieRepository;
    private final BookRepository bookRepository;
    private final MusicRepository musicRepository;
    private final EmbeddingModel embeddingModel;
    private final KeywordRepository keywordRepository;
    private final OllamaChatModel ollamaChatModel;

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
        keywordSelection.setKeywords(keywords);
        keywordRepository.save(keywordSelection);
    }

    @Transactional
    public List<Movie> cosineComputeMovie(String genre, String userId) {
        KeywordSelection keywordSelection = keywordRepository.findByUserId(userId);
        byte[] byteEmbedding = keywordSelection.getEmbedding();
        float[] floatEmbedding = toFloatArray(byteEmbedding);

        List<Movie> rawMovies = movieRepository.findByGenreName(genre);

        List<Movie> movies = rawMovies.stream()
                .peek(m -> {
                })
                .map(movie -> {
                    float similarity = CosineSimilarity.compute(
                            floatEmbedding,
                            toFloatArray(movie.getEmbedding())
                    );
                    return new AbstractMap.SimpleEntry<>(movie, similarity);
                })
                .sorted((a, b) -> Float.compare(b.getValue(), a.getValue()))
                .limit(8)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        String context = movies.stream()
                .map(m -> String.format("제목: %s\n줄거리: %s", m.getTitle(), m.getDescription()))
                .collect(Collectors.joining("\n\n"));
//        System.out.println("[DEBUG] 컨텍스트:\n" + context);

        String prompt = String.format("""
        [시스템]
        - 당신은 한국어 영화 추천 전문가입니다.
        - 반드시 제공된 영화 목록에서만 추천하세요.
        - 한국어로 답변하고, 4개를 추천하세요.
        - 출력 형식:
            1. 제목: [이유]
        
        [키워드]: %s
        
        [영화 목록]:
        %s
        """,
                keywordSelection.getKeywords(),
                context
        );

        try {
            String recommendation = ollamaChatModel.call(prompt);
            System.out.println("[추천 결과]\n" + recommendation);
        } catch (ResourceAccessException e) {
            try {
                throw new ServiceUnavailableException("서비스에 접근할 수 없습니다.");
            } catch (ServiceUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        }
        return movies;
    }

    @Transactional
    public Object cosineComputeBook(String userId) {
        KeywordSelection keywordSelection = keywordRepository.findByUserId(userId);
        byte[] byteEmbedding = keywordSelection.getEmbedding();
        float[] floatEmbedding = toFloatArray(byteEmbedding);
        List<Book> books = bookRepository.findAll().stream().map(book -> {
                    float similarity = CosineSimilarity.compute(
                            floatEmbedding,
                            toFloatArray(book.getEmbedding())
                    );
                    return new AbstractMap.SimpleEntry<>(book, similarity);
                }).sorted((a, b) -> Float.compare(b.getValue(), a.getValue()))
                .limit(8)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        String context = books.stream()
                .map(b -> String.format("제목: %s\n책소개: %s", b.getTitle(), b.getDescription()))
                .collect(Collectors.joining("\n\n"));
        System.out.println("[DEBUG] 컨텍스트:\n" + context);

        String prompt = String.format("""
                사용자 키워드: %s
                다음 도서 8권 중에서 가장 적합한 4개를 추천해주세요:
                %s
                """, keywordSelection.getKeywords(), context);

        try {
            String recommendation = ollamaChatModel.call(prompt);
            System.out.println("[추천 결과]\n" + recommendation);
        } catch (ResourceAccessException e) {
            try {
                throw new ServiceUnavailableException("서비스에 접근할 수 없습니다.");
            } catch (ServiceUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        }
        return books;
    }

    @Transactional
    public Object cosineComputeMusic(String userId) {
        KeywordSelection keywordSelection = keywordRepository.findByUserId(userId);
        byte[] byteEmbedding = keywordSelection.getEmbedding();
        float[] floatEmbedding = toFloatArray(byteEmbedding);
        List<Music> musics = musicRepository.findAll().stream().map(music -> {
                    float similarity = CosineSimilarity.compute(
                            floatEmbedding,
                            toFloatArray(music.getEmbedding())
                    );
                    return new AbstractMap.SimpleEntry<>(music, similarity);
                }).sorted((a, b) -> Float.compare(b.getValue(), a.getValue()))
                .limit(8)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        String context = musics.stream()
                .map(m -> String.format("제목: %s\n가사: %s", m.getTitle(), m.getLyrics()))
                .collect(Collectors.joining("\n\n"));
        System.out.println("[DEBUG] 컨텍스트:\n" + context);

        String prompt = String.format("""
                사용자 키워드: %s
                다음 노래 중 가장 적합한 4개를 추천해주세요:
                %s
                """, keywordSelection.getKeywords(), context);

        try {
            String recommendation = ollamaChatModel.call(prompt);
            System.out.println("[추천 결과]\n" + recommendation);
        } catch (ResourceAccessException e) {
            try {
                throw new ServiceUnavailableException("서비스에 접근할 수 없습니다.");
            } catch (ServiceUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        }
        return musics;
    }

    public static Set<Integer> toBinarySet(float[] vector) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] > 0.5) {
                set.add(i);
            }
        }
        return set;
    }
}

