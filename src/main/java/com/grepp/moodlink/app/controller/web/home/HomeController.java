package com.grepp.moodlink.app.controller.web.home;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping
    public String mainPage() {
        return "/home/mainPage";
    }
}
