package com.grepp.moodlink.app.controller.api.music;


import com.grepp.moodlink.app.model.data.music.MusicService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/music")
public class MusicApiController {

    private final MusicService musicService;

    // worldcup 생성 시 음악 목록 전달(추후 LLM으로 변경 예정)
    @GetMapping("/worldcup")
    public ResponseEntity<List<Map<String,Object>>> getMusics(){
        return ResponseEntity.ok(musicService.getMusicList());
    }
}
