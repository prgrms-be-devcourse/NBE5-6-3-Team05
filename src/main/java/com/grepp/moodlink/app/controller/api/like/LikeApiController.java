package com.grepp.moodlink.app.controller.api.like;

import com.grepp.moodlink.app.controller.api.like.payload.ToggleRequest;
import com.grepp.moodlink.app.controller.api.like.payload.ToggleResponse;
import com.grepp.moodlink.app.model.like.LikesService;
import com.grepp.moodlink.app.model.recomend.LikeService;
import com.grepp.moodlink.app.model.recomend.entity.Likes;
import java.util.List;
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

    private final LikesService likeService;
    private final LikeService likeServicejw;

    @PostMapping("/book/toggle")
    public ResponseEntity<ToggleResponse> toggleBook(@RequestBody ToggleRequest request) {
        boolean updated = likeService.toggleBookStatus(request.getId());
        return ResponseEntity.ok(new ToggleResponse(updated));
    }

    @PostMapping("/song/toggle")
    public ResponseEntity<ToggleResponse> toggleSong(@RequestBody ToggleRequest request, Authentication authentication) {
        boolean updated = likeServicejw.toggleLikeMusic(authentication.getName(), request.getId());
        System.out.println("controller status: "+ updated);
        return ResponseEntity.ok(new ToggleResponse(updated));
    }

    @PostMapping("/movie/toggle")
    public ResponseEntity<ToggleResponse> toggleMovie(@RequestBody ToggleRequest request) {
        boolean updated = likeService.toggleMovieStatus(request.getId());
        return ResponseEntity.ok(new ToggleResponse(updated));
    }
}
