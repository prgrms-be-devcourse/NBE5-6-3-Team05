package com.grepp.moodlink.app.controller.web.member;

import com.grepp.moodlink.app.controller.web.member.payload.ModifyRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {
    private final MemberService memberService;


    @GetMapping
    public String showMyPage(HttpSession session ,Model model){
//        String userId = (String) session.getAttribute("userId");
//        if(userId == null){
//            return "redirect:/";
//        }
//
//        Optional<MemberInfoDto> memberInfo = memberService.GetMemberInfo(userId);
//
//        if (memberInfo.isPresent()) {
//            MemberInfoDto info = memberInfo.get();
//            model.addAttribute("userId" , info.getId());
//            model.addAttribute("username", info.getUsername());
//            model.addAttribute("createdAt",info.getCreatedAt());
//            model.addAttribute("updatedAt", info.getUpdatedAt());
//            model.addAttribute("countries", info.getContries());
//
//        }

        return "/users/mypage";}

    @GetMapping("/modify")
    public String showModifyMyPage(HttpSession session, Model model){
//        String userId = (String) session.getAttribute("userId");
//        if(userId == null){
//            return "redirect:/";
//        }
//        model.addAttribute("userId", userId);
//        model.addAttribute("username", memberService.GetUsername(userId));
        return "/users/modify";

    }

    @PostMapping("/modify")
    public String modifyProfile(ModifyRequest request ,HttpSession session){
        String userId = (String) session.getAttribute("userId");
        if(userId == null) {
            return "redirect:/";
        }
        memberService.modifyProfile(userId, request.toDto());
        return "redirect:/users";
    }



    @GetMapping("/like")
    public String showLikePage(HttpSession session, Model model){
        return "/users/like";
    }




}
