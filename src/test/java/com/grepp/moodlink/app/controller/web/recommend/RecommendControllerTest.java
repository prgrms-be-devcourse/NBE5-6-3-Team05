package com.grepp.moodlink.app.controller.web.recommend;

import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.movie.MovieService;
import com.grepp.moodlink.app.model.data.music.MusicService;
import com.grepp.moodlink.app.model.result.dto.CuratingDetailIdDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RecommendControllerTest {
    @Autowired
    private RecommendController recommendController;
    @Autowired
    private MovieService movieService;
    @Autowired
    private BookService bookService;
    @Autowired
    private MusicService musicService;

    @Test
    public void keywordTest(){
        List<CuratingDetailIdDto> items = recommendController.curatingContents("오늘 기분은=기쁨이야.오늘 날씨는=맑음이야.지금 시간대는=아침이야.같이 있는 사람은=혼자이야.지금 필요한거는=행복");
    }
}