package com.grepp.moodlink.app.controller.web.member;

import com.grepp.moodlink.app.model.member.MemberRepository;
import com.grepp.moodlink.app.model.member.MemberService;
import com.grepp.moodlink.app.model.member.dto.MemberInfoDto;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {
    private final MemberService memberService;


    @GetMapping("/")
    private String showMyPage(HttpSession session ,Model model){
        String userId = (String) session.getAttribute("userId");
        Optional<MemberInfoDto> memberInfo = memberService.GetMemberInfo(userId);
        if (memberInfo.isPresent()) {
            MemberInfoDto info = memberInfo.get();
            model.addAttribute(info.getId());
            model.addAttribute(info.getUsername());
            model.addAttribute(info.getCreatedAt());
            model.addAttribute(info.getUpdatedAt());
            model.addAttribute(info.getContries());

        } else {
            // 값이 없을 때 처리
        }


        return "/users";}






}
