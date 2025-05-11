package com.grepp.moodlink.infra.app.controller.api.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/like/{contentId}")
public class LikeApiController {
    //TODO: 각 컨텐츠의 상태에 따라 토글 형식으로 like_table에 추가/삭제 (PostMapping)

}
