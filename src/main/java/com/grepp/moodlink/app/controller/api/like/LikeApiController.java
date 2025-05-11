package com.grepp.moodlink.app.controller.api.like;

import com.grepp.moodlink.app.controller.api.like.payload.ToggleRequest;
import com.grepp.moodlink.app.controller.api.like.payload.ToggleResponse;
import com.grepp.moodlink.app.model.like.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    //TODO: 각 컨텐츠의 상태에 따라 토글 형식으로 like_table에 추가/삭제 (PostMapping)


    @PostMapping("/book/toggle")
    public ResponseEntity<ToggleResponse> toggleBook(@RequestBody ToggleRequest request) {
        boolean updated = likeService.toggleBookStatus(request.getId());
        return ResponseEntity.ok(new ToggleResponse(updated));
    }


    @PostMapping("/song/toggle")
    public ResponseEntity<ToggleResponse> toggleSong(@RequestBody ToggleRequest request) {
        boolean updated = likeService.toggleSongStatus(request.getId());
        return ResponseEntity.ok(new ToggleResponse(updated));
    }


    @PostMapping("/movie/toggle")
    public ResponseEntity<ToggleResponse> toggleMovie(@RequestBody ToggleRequest request) {
        boolean updated = likeService.toggleMovieStatus(request.getId());
        return ResponseEntity.ok(new ToggleResponse(updated));
    }
}
