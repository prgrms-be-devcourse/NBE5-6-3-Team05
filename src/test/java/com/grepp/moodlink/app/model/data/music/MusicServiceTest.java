package com.grepp.moodlink.app.model.data.music;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MusicServiceTest {
    @Autowired
    private MusicService musicService;

    @Test
    public void parsingTest(){
        System.out.println(musicService.parseRecommend("노래\"아니 근데 진짜 (Unbelievable)\", \"Phonecert\", \"노메이크업\", \"200%\", \"나는 너 좋아 (I Like You)\", \"너에게 닿기를 (To Reach You)\", \"OMG\", \"꺼내 먹어요 (Eat)\""));
    }
}