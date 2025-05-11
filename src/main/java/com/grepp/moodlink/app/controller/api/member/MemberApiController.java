package com.grepp.moodlink.app.controller.api.member;

import com.grepp.moodlink.app.model.book.dto.BookDto;
import com.grepp.moodlink.app.model.recomend.LikeService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("users")
@RequiredArgsConstructor
public class MemberApiController {

    private final LikeService likeService;

    @GetMapping("/like/{Book}")
    public ResponseEntity<List<BookDto>> getUserLikedBooks(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<BookDto> likedBooks = likeService.getUserLikedBooks(userId);
        return ResponseEntity.ok(likedBooks);
    }



}
