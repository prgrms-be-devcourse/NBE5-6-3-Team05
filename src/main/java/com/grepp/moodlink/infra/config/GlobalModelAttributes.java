package com.grepp.moodlink.infra.config;

import com.grepp.moodlink.app.model.home.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final HomeService homeService;

    @ModelAttribute
    public void globalAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken);

        model.addAttribute("isAuthenticated", isAuthenticated);
        if (isAuthenticated) {
            String username = homeService.findUsername(authentication.getName());
            model.addAttribute("username", username);
        }
    }
}

