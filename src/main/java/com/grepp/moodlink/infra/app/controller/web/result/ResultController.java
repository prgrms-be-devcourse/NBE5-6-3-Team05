package com.grepp.moodlink.infra.app.controller.web.result;


import com.grepp.moodlink.infra.app.model.result.ResultService;
import com.grepp.moodlink.infra.app.model.result.dto.CuratingDetailDto;
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
    public String result(Model model){
        // TODO : result 화면 출력 내용물 가져오기

        // Service에서 가져올 것
            // 회원과 1:1 인 curating 가져오기
            // List<curating_detail> curating_details 가져오기
            // 각 curating_detail에서 영화, 음악, 도서 한 개씩 제목과 이미지 가져오기
            // model에 추가

        List<CuratingDetailDto> tempList = resultService.curatingDetailDtoList();
        System.out.println(tempList);
        // TODO : curating reason 출력하기 // RecommendController와 상의 필요
        model.addAttribute("curatingReason","원시천존 원시천존.. 이것은 curatingReason출력문");
        model.addAttribute("items", tempList);
        return "result/result";
    }

    @GetMapping("recommend")
    public String toRecommend(){
        return "/recommend/recommend";
    }



    // ################################################################# header 내용
    @GetMapping("mypage")
    public String toUser(){

        return "/member/mypage";
    }

    @GetMapping("main")
    public String toMain(){

        return "/main/main";
    }

    @GetMapping("signin")
    public String toSignin(){

        return "/member/signin";
    }
}
