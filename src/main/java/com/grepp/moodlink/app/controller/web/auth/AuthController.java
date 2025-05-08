package com.grepp.moodlink.app.controller.web.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/signup")
    public String signup() {
        return "auth/signup";
    }

    @GetMapping("/signin")
    public String signin() {
        return "auth/signin";
    }

}
