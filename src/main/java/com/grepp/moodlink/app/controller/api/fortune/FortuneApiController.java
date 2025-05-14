package com.grepp.moodlink.app.controller.api.fortune;

import com.grepp.moodlink.app.model.home.FortuneAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FortuneApiController {

    private final FortuneAiService fortuneAiService;

    // 운세를 반환하는 API
    @GetMapping("/fortune")
    public String getFortune() {
        return fortuneAiService.generateFortune();
    }

    // AI로 운세를 다시 받아오는 API
    @PostMapping("/fortune/ai")
    public String getFortuneFromAi() {
        return fortuneAiService.generateFortune();
    }

}
