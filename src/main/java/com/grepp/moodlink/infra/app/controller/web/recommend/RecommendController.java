package com.grepp.moodlink.infra.app.controller.web.recommend;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("recommend")
public class RecommendController {

    @GetMapping
    public String result(Model model){

        return "recommend/recommend";
    }
}
