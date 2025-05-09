package com.grepp.moodlink.app.controller.web.home;

import com.grepp.moodlink.app.model.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @GetMapping
    public String mainPage() {
        return "/home/mainPage";
    }
}
