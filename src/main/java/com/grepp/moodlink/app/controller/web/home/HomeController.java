package com.grepp.moodlink.app.controller.web.home;

import com.grepp.moodlink.app.model.home.HomeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public String mainPage(Model model) {
        List<String> thumbnail = homeService.showContent();
        List<String> people = homeService.findPeople();
        List<String> title = homeService.findTitle();

        model.addAttribute("thumbnail", thumbnail);
        model.addAttribute("people", people);
        model.addAttribute("title", title);

        return "/home/mainPage";
    }

    @GetMapping("/worldcup")
    public String worldcupPage(Model model) {
        return "/home/worldcup";
    }

}
