package com.grepp.moodlink.app.controller.web.auth;

import com.grepp.moodlink.app.controller.web.auth.payload.SigninRequest;
import com.grepp.moodlink.app.controller.web.auth.payload.SignupRequest;
import com.grepp.moodlink.app.model.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String getSignup(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String postSignup(SignupRequest signupRequest) {
        memberService.signup(signupRequest.toDto());
        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String getSignin(Model model) {
        model.addAttribute("signinRequest", new SigninRequest());
        return "auth/signin";
    }

}
