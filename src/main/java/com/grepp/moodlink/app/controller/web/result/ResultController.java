package com.grepp.moodlink.app.controller.web.result;


import com.grepp.moodlink.app.model.result.ResultService;
import com.grepp.moodlink.app.model.result.dto.CuratingDetailDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String result(Model model) {

        List<CuratingDetailDto> tempList = resultService.curatingDetailDtoList();

        String msg1 ="curatingReason출력문 예시 <br>";
        String msg2 ="오늘 해고당해서 우울한 당신! 고단한 하루를 달래줄 명작을 소개해드릴게요. <br>";
        String msg3 ="다음 작품들은 위로가 될 거예요!";

        model.addAttribute("curatingReason", msg1+msg2+msg3);
        model.addAttribute("items", tempList);
        return "result/result";
    }
}