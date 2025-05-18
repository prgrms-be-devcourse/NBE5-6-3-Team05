package com.grepp.moodlink.app.controller.api.like;

import com.grepp.moodlink.app.controller.api.like.payload.ToggleRequest;
import com.grepp.moodlink.app.controller.api.like.payload.ToggleResponse;
import com.grepp.moodlink.app.model.recomend.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/like")
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("/book/toggle")
    public ResponseEntity<ToggleResponse> toggleBook(@RequestBody ToggleRequest request, Authentication authentication) {

        String userId;
        userId = "";
        if (authentication != null){
            userId = authentication.getName();
        }

        // 비회원 시 바로 false를 반환(좋아요 비활성화 유지)
        if (userId.isEmpty()){
            return ResponseEntity.ok(new ToggleResponse(false));
        }

        boolean updated = likeService.toggleLikeBook(userId, request.getId());
        return ResponseEntity.ok(new ToggleResponse(updated));
    }

    @PostMapping("/song/toggle")
    public ResponseEntity<ToggleResponse> toggleSong(@RequestBody ToggleRequest request, Authentication authentication) {
        String userId;
        userId = "";
        if (authentication != null){
            userId = authentication.getName();
        }

        // 비회원 시 바로 false를 반환(좋아요 비활성화 유지)
        if (userId.isEmpty()){
            return ResponseEntity.ok(new ToggleResponse(false));
        }

        boolean updated = likeService.toggleLikeMusic(userId, request.getId());
        return ResponseEntity.ok(new ToggleResponse(updated));
    }

    @PostMapping("/movie/toggle")
    public ResponseEntity<ToggleResponse> toggleMovie(@RequestBody ToggleRequest request, Authentication authentication) {
        String userId;
        userId = "";
        if (authentication != null){
            userId = authentication.getName();
        }

        // 비회원 시 바로 false를 반환(좋아요 비활성화 유지)
        if (userId.isEmpty()){
            return ResponseEntity.ok(new ToggleResponse(false));
        }

        boolean updated = likeService.toggleLikeMovie(userId,request.getId());
        return ResponseEntity.ok(new ToggleResponse(updated));
    }
}
