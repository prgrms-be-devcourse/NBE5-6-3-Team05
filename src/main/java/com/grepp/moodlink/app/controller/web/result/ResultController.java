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
        // 변경할 내용: Session을 통해 Role을 얻어내고, 그에 따른 service내부의 logic 변경
        List<CuratingDetailDto> tempList = resultService.curatingDetailDtoList();

        // 변경할 내용: curating reason을 Session을 통해 가져오기
        model.addAttribute("curatingReason", "원시천존 원시천존.. 이것은 curatingReason출력문");

        model.addAttribute("items", tempList);
        return "result/result";
    }
}