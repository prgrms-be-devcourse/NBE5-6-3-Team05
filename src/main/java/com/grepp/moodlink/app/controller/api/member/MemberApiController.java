package com.grepp.moodlink.app.controller.api.member;


import com.grepp.moodlink.app.model.recomend.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("users")
@RequiredArgsConstructor
public class MemberApiController {

    private final LikeService likeService;


}
