package com.grepp.moodlink.app.controller.api.member;


import com.grepp.moodlink.app.controller.api.like.payload.ToggleRequest;
import com.grepp.moodlink.app.controller.api.like.payload.ToggleResponse;
import com.grepp.moodlink.app.model.recomend.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberApiController {

    private final LikeService likeService;

    @PostMapping("/like/books")
    public ResponseEntity<ToggleResponse> toggleBook(@RequestBody ToggleRequest request,
        Authentication authentication) {

        //TODO: 비회원 시 바로 return
        String userId;
        userId = "anonymous";
        if (authentication != null) {
            userId = authentication.getName();
        }
        if (userId.equals("anonymous")) {
            return ResponseEntity.ok(new ToggleResponse(false));
        }

        boolean updated = likeService.toggleLikeBook(userId, request.getId());
        return ResponseEntity.ok(new ToggleResponse(updated));
    }

    @PostMapping("/like/musics")
    public ResponseEntity<ToggleResponse> toggleSong(@RequestBody ToggleRequest request,
        Authentication authentication) {
        //TODO: 비회원 시 바로 return
        String userId;
        userId = "anonymous";
        if (authentication != null) {
            userId = authentication.getName();
        }
        if (userId.equals("anonoymous")) {
            return ResponseEntity.ok(new ToggleResponse(false));
        }

        boolean updated = likeService.toggleLikeMusic(userId, request.getId());
        return ResponseEntity.ok(new ToggleResponse(updated));
    }

    @PostMapping("/like/movies")
    public ResponseEntity<ToggleResponse> toggleMovie(@RequestBody ToggleRequest request,
        Authentication authentication) {
        //TODO: 비회원 시 바로 return
        String userId;
        userId = "anonymous";
        if (authentication != null) {
            userId = authentication.getName();
        }
        if (userId.equals("anonoymous")) {
            return ResponseEntity.ok(new ToggleResponse(false));
        }

        boolean updated = likeService.toggleLikeMovie(userId, request.getId());
        return ResponseEntity.ok(new ToggleResponse(updated));
    }
}
