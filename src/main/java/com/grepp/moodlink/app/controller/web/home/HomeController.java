package com.grepp.moodlink.app.controller.web.home;
import com.grepp.moodlink.app.model.home.HomeService;
import java.security.Principal;
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
        model.addAttribute("thumbnail",thumbnail);return "/home/mainPage";
    }
}
