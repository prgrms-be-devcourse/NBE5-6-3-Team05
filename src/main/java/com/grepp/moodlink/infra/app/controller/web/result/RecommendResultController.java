package com.grepp.moodlink.infra.app.controller.web.result;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("recommend/result")
public class RecommendResultController {

    @GetMapping
    public String result(Model model){
        model.addAttribute("curatingReason","원시천존 원시천존.. 이것은 curatingReason출력문");
        return "recommend/result";
    }

    @GetMapping("recommend")
    public String toRecommend(Model model){

        return "/recommend/recommend";
    }

    @GetMapping("mypage")
    public String toUser(Model model){

        return "/member/mypage";
    }

    @GetMapping("main")
    public String toMain(Model model){

        return "/main/main";
    }

    @GetMapping("signin")
    public String toSignin(Model model){

        return "/member/signin";
    }
}
