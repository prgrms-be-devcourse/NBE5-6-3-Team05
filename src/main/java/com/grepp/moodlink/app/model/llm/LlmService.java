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
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@Service
@RequiredArgsConstructor
@Slf4j
public class LlmService {

    private final MovieRepository movieRepository;
    private final BookRepository bookRepository;
    private final MusicRepository musicRepository;
    private final KeywordRepository keywordRepository;
    private final ChatLanguageModel chatLanguageModel;

    // 컨텐츠 추천 이유
    public String generateReason(String keywords) {
        KeywordSelection keywordSelection = keywordRepository.findByKeywords(keywords);

        String prompt = String.format("""
            [시스템]
            - 당신은 컨텐츠 추천 이유 전문가입니다.
            - 키워드를 바탕으로 어떤 컨텐츠를 추천해야하는지 이유를 말해주세요.
            - 하나의 이유만 추천하세요.
            - 절대 컨텐츠 종류나 이름을 직접 언급하지 마세요.
            - 출력 예시:
                오늘 해고당해서 우울한 당신! 고단한 하루를 달래줄 명작을 소개해드릴게요.
                다음 작품들은 위로가 될 거예요!
            
            [키워드]: %s
            
            """, keywordSelection.getKeywords());
        String recommendation;
        try {
            recommendation = chatLanguageModel.chat(prompt);
        } catch (ResourceAccessException e) {
            throw new LLMServiceUnavailableException(ResponseCode.EXTERNAL_API_UNAVAILABLE, e);
        }
        return recommendation;
    }

    public String recommendMovie(String keywords) {
        KeywordSelection keywordSelection = keywordRepository.findByKeywords(keywords);
    // 영화 추천
        byte[] byteEmbedding = keywordSelection.getEmbedding();
        float[] floatEmbedding = toFloatArray(byteEmbedding);

        List<Movie> movies = movieRepository.findAll().stream().map(movie -> {
                float similarity = CosineSimilarity.compute(
                    floatEmbedding,
                    toFloatArray(movie.getEmbedding())
                );
                return new AbstractMap.SimpleEntry<>(movie, similarity);
            })
            .sorted((a, b) -> Float.compare(b.getValue(), a.getValue()))
            .limit(40)
            .map(Map.Entry::getKey)
            .toList();

        String context = movies.stream()
            .map(m -> String.format("제목: %s\n영화소개: %s", m.getTitle(), m.getSummary()))
            .collect(Collectors.joining("\n\n"));

        String result = llmRecommend("영화", keywordSelection.getKeywords(), context);

        System.out.println("영화" + result);

        return result;
    }

    // 도서 추천
    public String recommendBook(String keywords) {
        KeywordSelection keywordSelection = keywordRepository.findByKeywords(keywords);
        byte[] byteEmbedding = keywordSelection.getEmbedding();
        float[] floatEmbedding = toFloatArray(byteEmbedding);
        List<Book> books = bookRepository.findAll().stream().map(book -> {
                float similarity = CosineSimilarity.compute(
                    floatEmbedding,
                    toFloatArray(book.getEmbedding())
                );
                return new AbstractMap.SimpleEntry<>(book, similarity);
            }).sorted((a, b) -> Float.compare(b.getValue(), a.getValue()))
            .limit(40)
            .map(Map.Entry::getKey)
            .toList();

        String context = books.stream()
            .map(b -> String.format("제목: %s\n책소개: %s", b.getTitle(), b.getSummary()))
            .collect(Collectors.joining("\n\n"));

        String result = llmRecommend("도서", keywordSelection.getKeywords(), context);

        System.out.println("도서" + result);

        return result;
    }

    // 음악 추천
    public String recommendMusic(String keywords) {
        KeywordSelection keywordSelection = keywordRepository.findByKeywords(keywords);
        byte[] byteEmbedding = keywordSelection.getEmbedding();
        float[] floatEmbedding = toFloatArray(byteEmbedding);
        List<Music> musics = musicRepository.findAll().stream().map(music -> {
                float similarity = CosineSimilarity.compute(
                    floatEmbedding,
                    toFloatArray(music.getEmbedding())
                );
                return new AbstractMap.SimpleEntry<>(music, similarity);
            }).sorted((a, b) -> Float.compare(b.getValue(), a.getValue()))
            .limit(40)
            .map(Map.Entry::getKey)
            .toList();

        String context = musics.stream()
            .map(m -> String.format("제목: %s\n노래소개: %s", m.getTitle(), m.getSummary()))
            .collect(Collectors.joining("\n\n"));

        String result = llmRecommend("노래", keywordSelection.getKeywords(), context);

        System.out.println("노래" + result);

        return result;
    }

    // 콘텐츠 추천 llm 호출
    private String llmRecommend(String category, String keywords, String context) {
        String prompt = String.format("""
            [시스템]
            - 당신은 %s 추천 전문가입니다.
            - 반드시 제공된 목록에서만 추천하세요.
            - 한국어로만 답변하고, 4개를 추천하세요.
            - 제공된 목록에서 제목만 출력하세요.
            - 각 제목은 '"'로 감싸져있고, ','로 구분하여 출력하세요.
            - 출력 형식:
                "[%s 제목]", "[%s 제목]", "[%s 제목]", "[%s 제목]"
            
            [키워드]: %s
            
            [%s 목록]:
            %s
            """, category, category, category, category, category, keywords, category, context);
        String recommendation;
        try {
            recommendation = chatLanguageModel.chat(prompt);
        } catch (ResourceAccessException e) {
            throw new LLMServiceUnavailableException(ResponseCode.EXTERNAL_API_UNAVAILABLE, e);
        }
        return recommendation;
    }

    // 임베딩 값 변환
    private float[] toFloatArray(byte[] bytes) {
        FloatBuffer buffer = ByteBuffer.wrap(bytes).asFloatBuffer();
        float[] floats = new float[buffer.remaining()];
        buffer.get(floats);
        return floats;
    }

}
