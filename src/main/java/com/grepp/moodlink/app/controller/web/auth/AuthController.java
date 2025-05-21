package com.grepp.moodlink.app.controller.web.auth;

import com.grepp.moodlink.app.controller.web.auth.payload.SigninRequest;
import com.grepp.moodlink.app.controller.web.auth.payload.SignupRequest;
import com.grepp.moodlink.app.model.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String postSignup(@Valid SignupRequest signupRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }
        try {
            memberService.signup(signupRequest.toDto());
        } catch (IllegalArgumentException e) {
            bindingResult.reject(null, e.getMessage());
            return "auth/signup";
        }
        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String getSignin(Model model) {
        model.addAttribute("signinRequest", new SigninRequest());
        return "auth/signin";
    }
}
