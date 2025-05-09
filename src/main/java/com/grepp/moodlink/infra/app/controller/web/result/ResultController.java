package com.grepp.moodlink.infra.app.controller.web.result;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("result")
public class ResultController {

    @GetMapping
    public String result(Model model){
        model.addAttribute("curatingReason","원시천존 원시천존.. 이것은 curatingReason출력문");
        return "result/result";
    }

    @GetMapping("recommend")
    public String toRecommend(){

        return "/recommend/recommend";
    }

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
