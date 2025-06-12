package com.grepp.moodlink.app.controller.web.worldcup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// worldcup 페이지들
@RequestMapping("/worldcup")
@Controller
@RequiredArgsConstructor
public class WorldcupController {

    // worldcup 페이지
    @GetMapping
    public String worldcupPage() {
        return "/worldcup/worldcup";
    }

    // worldcup 생성 페이지
    @GetMapping("/create")
    public String worldcupCreate(){
        return "worldcup/create";
    }

    // worldcup 플레이 페이지
    @GetMapping("/play")
    public String worldcupPlay(){
        return "worldcup/play";
    }

    // worldcup 결과 페이지
    @GetMapping("/result")
    public String showResultPage() {
        return "worldcup/result";
    }
}
