package com.grepp.moodlink.app.controller.web.result;


import com.grepp.moodlink.app.model.result.ResultService;
import com.grepp.moodlink.app.model.result.dto.CuratingDetailDto;
import com.grepp.moodlink.app.model.result.dto.CuratingDetailIdDto;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("result")
public class ResultController {

    private final ResultService resultService;

    @GetMapping
    public String result(Model model, Authentication authentication, HttpSession session) {
        String userId ="";
        if (authentication != null){
            userId = authentication.getName();
        }

        List<CuratingDetailIdDto> recommendResult = (List<CuratingDetailIdDto>) session.getAttribute("items");
        if (recommendResult == null){
            recommendResult = List.of();
        }

        String reason = (String) session.getAttribute("reason");
        if (reason == null){
            reason = "기본 이유입니다.아이유";
        }

        List<CuratingDetailDto> tempList = resultService.curatingDetailDtoList(userId, recommendResult);

        model.addAttribute("curatingReason", reason);
        model.addAttribute("items", tempList);
        return "result/result";
    }
}