package com.grepp.moodlink.app.model.llm.batch;

import com.google.common.util.concurrent.RateLimiter;
import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.movie.MovieService;
import com.grepp.moodlink.app.model.data.music.MusicService;
import com.grepp.moodlink.app.model.llm.LlmService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendationProcessor implements ItemProcessor<String, RecommendationDto> {

    private final LlmService llmService;
    private final MovieService movieService;
    private final BookService bookService;
    private final MusicService musicService;
    private static final RateLimiter ratelimiter = RateLimiter.create(3.0 / 60.0);

    @Override
    public RecommendationDto process(String keywords){
        ratelimiter.acquire();
        List<String> movieRecommendations = movieService.parseRecommend(llmService.recommendMovie(keywords));
        List<String> bookRecommendations = bookService.parseRecommend(llmService.recommendBook(keywords));
        List<String> musicRecommendations = musicService.parseRecommend(llmService.recommendMusic(keywords));
        String reason = llmService.generateReason(keywords);

        return new RecommendationDto(keywords, reason, movieRecommendations, bookRecommendations, musicRecommendations);
    }
}
