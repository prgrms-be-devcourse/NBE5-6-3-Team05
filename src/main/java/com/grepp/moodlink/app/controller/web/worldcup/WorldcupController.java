package com.grepp.moodlink.app.controller.web.worldcup;

import com.grepp.moodlink.app.controller.web.worldcup.Response.WorldcupResultResponse;
import com.grepp.moodlink.app.model.worldcup.WorldcupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// worldcup 페이지들
@RequestMapping("/worldcup")
@Controller
@RequiredArgsConstructor
public class WorldcupController {
    private final WorldcupService worldcupService;

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
    public String showResultPage(@RequestParam("worldcupId") Long worldcupId,
        @RequestParam("winner") String winnerId,
        Model model) {

        // 서비스 호출해서 우승자 정보 조회
        WorldcupResultResponse worldcupResultResponse = worldcupService.getStatistics(worldcupId,winnerId);

        // 월드컵 갯수, 같은 우승자 수 model에 담아서 view로 전달
        model.addAttribute("worldcupCount", worldcupResultResponse.getTotalCount());
        model.addAttribute("winnerCount", worldcupResultResponse.getWinCount());
        System.out.println(worldcupResultResponse);
        return "worldcup/result";
    }
}
