package com.grepp.moodlink.app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/recommend")
@Controller
public class IndexController {
    @GetMapping
    public String index(){
        return "recommend";
    }
}
