package com.grepp.moodlink.app.controller.web.auth;

import com.grepp.moodlink.app.controller.web.auth.payload.SigninRequest;
import com.grepp.moodlink.app.controller.web.auth.payload.SignupRequest;
import com.grepp.moodlink.app.model.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String getSignup(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String postSignup(@Valid SignupRequest signupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        if (memberService.existsByUserId(signupRequest.getUserId())) {
            bindingResult.rejectValue("userId", "duplicate", "이미 가입된 회원입니다.");
            return "auth/signup";
        }

        memberService.signup(signupRequest.toDto());

        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String getSignin(Model model) {
        model.addAttribute("signinRequest", new SigninRequest());
//        log.info("로그인 창");
        return "auth/signin";
    }
}
