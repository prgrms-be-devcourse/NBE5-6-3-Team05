package com.grepp.moodlink.app.controller.web.recommend;

import com.grepp.moodlink.app.model.auth.domain.Principal;
import com.grepp.moodlink.app.model.embedding.EmbeddingService;
import com.grepp.moodlink.app.model.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/recommend")
@Controller
@RequiredArgsConstructor
public class RecommendController {

    private final EmbeddingService embeddingService;
    private final KeywordService keywordService;

    @GetMapping
    public String selectKeyword(){
        return "/recommend/recommend";
    }

    @PostMapping("result")
    public String selectKeyword(@RequestParam("keywords") String keywords, Model model){
        String userId = getLoginUserId();
        keywordService.generateKeywordSelection(userId);
        embeddingService.generateEmbeddingKeyword(userId, keywords);
        return "home/mainPage";
    }

    private String getLoginUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        Principal principal = (Principal) auth.getPrincipal();
        return principal.getUsername();
    }
}
