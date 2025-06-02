package com.grepp.moodlink.app.model.llm;

import com.grepp.moodlink.app.model.data.book.BookRepository;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.movie.MovieRepository;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.music.MusicRepository;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.keyword.KeywordRepository;
import com.grepp.moodlink.app.model.keyword.entity.KeywordSelection;
import com.grepp.moodlink.infra.error.LLMServiceUnavailableException;
import com.grepp.moodlink.infra.response.ResponseCode;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import jakarta.transaction.Transactional;
import java.nio.ByteBuffer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {

    private final MovieRepository movieRepository;
    private final BookRepository bookRepository;
    private final MusicRepository musicRepository;
    private final EmbeddingModel embeddingModel;
    private final KeywordRepository keywordRepository;
    private final ChatLanguageModel chatLanguageModel;

    @Transactional
    @Async
    @Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void generateEmbeddingsMovie() {
        List<Movie> movies = movieRepository.findByEmbeddingIsNull();
        for (Movie movie : movies) {
            String text = "영화 제목 : " + movie.getTitle()
                + "영화 설명" + movie.getDescription();
            float[] floatEmbedding = embeddingModel.embed(text).content().vector();
            byte[] byteEmbedding = toByteArray(floatEmbedding);
            movie.setEmbedding(byteEmbedding);

            String prompt = String.format(
                "영화줄거리를 보고 한두 문장으로 요약해줘. 핵심 주제, 분위기, 특징을 중심으로 간결하게 써줘.\n\n[설명]: %s",
                movie.getDescription()
            );
            String summary;
            try {
                summary = chatLanguageModel.chat(prompt);
            } catch (ResourceAccessException e) {
                throw new LLMServiceUnavailableException(ResponseCode.EXTERNAL_API_UNAVAILABLE, e);
            }
            movie.setSummary(summary);
            movieRepository.save(movie);
        }
    }

    @Transactional
    @Async
    @Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void generateEmbeddingsBook() {
        List<Book> books = bookRepository.findByEmbeddingIsNull();
        for (Book book : books) {
            String text = "도서 제목 : " + book.getTitle()
                + "도서 설명 :" + book.getDescription();
            float[] floatEmbedding = embeddingModel.embed(text).content().vector();
            byte[] byteEmbedding = toByteArray(floatEmbedding);
            book.setEmbedding(byteEmbedding);

            String prompt = String.format(
                "책소개를 보고 한두 문장으로 요약해줘. 핵심 주제, 분위기, 특징을 중심으로 간결하게 써줘.\n\n[설명]: %s",
                book.getDescription()
            );
            String summary;
            try {
                summary = chatLanguageModel.chat(prompt);
            } catch (ResourceAccessException e) {
                throw new LLMServiceUnavailableException(ResponseCode.EXTERNAL_API_UNAVAILABLE, e);
            }
            book.setSummary(summary);
            bookRepository.save(book);
        }
    }

    @Transactional
    @Async
    @Retryable(
        maxAttempts = 5,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void generateEmbeddingsMusic() {
        List<Music> musics = musicRepository.findByEmbeddingIsNull();
        for (Music music : musics) {
            String text = "노래 제목 : " + music.getTitle()
                + "노래 가사 : " + music.getLyrics();
            float[] floatEmbedding = embeddingModel.embed(text).content().vector();

            byte[] byteEmbedding = toByteArray(floatEmbedding);
            music.setEmbedding(byteEmbedding);
            String prompt = String.format(
                "가사를 보고 한두 문장으로 요약해줘. 핵심 주제, 분위기, 특징을 중심으로 간결하게 써줘.\n\n[설명]: %s",
                music.getLyrics()
            );
            String summary;
            try {
                summary = chatLanguageModel.chat(prompt);
            } catch (ResourceAccessException e) {
                throw new LLMServiceUnavailableException(ResponseCode.EXTERNAL_API_UNAVAILABLE, e);
            }
            music.setSummary(summary);

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

    public void generateEmbeddingKeyword(String userId, String keywords) {
        KeywordSelection keywordSelection = keywordRepository.findByUserId(userId);
        float[] floatEmbedding = embeddingModel.embed(keywords).content().vector();
        byte[] byteEmbedding = toByteArray(floatEmbedding);
        keywordSelection.setEmbedding(byteEmbedding);
        keywordSelection.setKeywords(keywords);
        keywordRepository.save(keywordSelection);
    }

}

