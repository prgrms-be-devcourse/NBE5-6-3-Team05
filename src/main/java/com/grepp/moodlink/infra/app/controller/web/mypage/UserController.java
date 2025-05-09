package com.grepp.moodlink.infra.app.controller.web.mypage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("member")
public class UserController {

    @GetMapping("signin")
    public String signin(Model model){

        return "member/signin";
    }

    @GetMapping("mypage")
    public String result(Model model){

        return "member/mypage";
    }
}
