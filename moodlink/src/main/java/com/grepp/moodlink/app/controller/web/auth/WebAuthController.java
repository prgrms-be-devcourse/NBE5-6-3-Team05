package com.grepp.moodlink.app.controller.web.auth;

import com.grepp.moodlink.app.controller.web.auth.payload.SigninRequest;
import com.grepp.moodlink.app.controller.web.auth.payload.SignupRequest;
import com.grepp.moodlink.app.model.member.MemberService;
import com.grepp.moodlink.infra.response.ApiResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String getSignup(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "auth/signup";
    }

    // verify 하여 인증 이메일 보내기
    @PostMapping("/signup")
    public String postSignupVerify(@Valid SignupRequest signupRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        if (memberService.existsByUserId(signupRequest.getUserId())) {
            bindingResult.rejectValue("userId", "duplicate", "이미 가입된 회원입니다.");
            return "auth/signup";
        }

        String token = session.getId();
        session.setAttribute(token,signupRequest);

        // 이메일 발송
        memberService.verify(signupRequest.toDto(), token);

        return "redirect:/verification";
    }

    @GetMapping("/verification")
    public String verification(){
        return "auth/verification";
    }
    
    @GetMapping("/complete")
    public String complete(Model model){
        model.addAttribute("message", "회원 가입이 완료되었습니다!");
        return "auth/complete";
    }

    // 이메일에서 confirm을 누를 시 회원 가입 완료
    @GetMapping("add/{token}")
    public String signin(
        @PathVariable
        String token,
        HttpSession session,
        Model model
    ){
        Object userInfo = session.getAttribute(token);
        if(userInfo == null) {
            model.addAttribute("message", "오류가 발생하였습니다. 다시 시도해주세요.");
            return "auth/complete";
        }
        SignupRequest request = (SignupRequest) userInfo;
        memberService.signup(request.toDto());

        // 회원 가입 후에 세션 삭제
        session.removeAttribute(token);

        return "redirect:/complete";
    }

    @GetMapping("/signin")
    public String getSignin(Model model) {
        model.addAttribute("signinRequest", new SigninRequest());
        return "auth/signin";
    }
}
