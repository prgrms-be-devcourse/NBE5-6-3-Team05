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
    public String recommendMovie(String genre, String userId) {
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
        System.out.println(context);

//        return "[ 추천 결과 ]\n" +
//                "1. \"봄 여름 가을 겨울 그리고 봄\" : 이 영화는 사랑과 죄책감, 복수와 사회적 배려에 대한 심각하고 진정한 의미를 담은 작품으로, 나의 지금 상황인 '혼자'와 '밤' 때문에 더욱 감동적이라고 생각됩니다.\n" +
//                "2. \"소원\" : 이 영화는 어린 아이의 상처를 받은 일에 대한 희망을 찾으려는 모습에, '지칫' 때문에 더욱 감동적이라고 생각됩니다.\n" +
//                "3. \"복수는 나의 것\" : 이 영화는 혼자서 지내는 상황에서, '위로'가 필요한 상황이라면 좋은 선택이 될 것입니다. 숙희의 강인함과 결단력을 보며, 나의 겁을 덜어주는 영화가 되었습니다.\n" +
//                "4. \"암살\" : 이 영화는 '비'가 오는 날씨에서, '같이 있는 사람'이 없는 상황인데도 불구하고 강인함과 용기를 느껴보고자 하시면 좋은 선택입니다. 안옥윤, 속사포, 황덕삼의 용기와 강인함을 감상할 수 있습니다.\n";
        return llmRecommend("영화", keywordSelection.getKeywords(), context);
    }

    @Transactional
    public String recommendBook(String userId) {
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

//        return llmRecommend("도서", keywordSelection.getKeywords(), context);
        return "";
    }

    @Transactional
    public String recommendMusic(String userId) {
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


//        return llmRecommend("노래", keywordSelection.getKeywords(), context);
        return "";
    }

    private String llmRecommend(String category, String keywords, String context) {
        String prompt = String.format("""
                [시스템]
                - 당신은 %s 추천 전문가입니다.
                - 반드시 제공된 %s 목록에서만 추천하세요.
                - 한국어로만 답변하고, 4개를 추천하세요.
                - 출력 형식:
                    1. [%s 제목] : [추천 이유]
                
                [키워드]: %s
                
                [%s 목록]:
                %s
                """, category, category, category, keywords, category, context);
        String recommendation = null;
        try {
            recommendation = ollamaChatModel.call(prompt);
        } catch (ResourceAccessException e) {
            try {
                throw new ServiceUnavailableException("서비스에 접근할 수 없습니다.");
            } catch (ServiceUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        }
        return recommendation;
    }
}

