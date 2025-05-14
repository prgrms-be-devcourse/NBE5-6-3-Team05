package com.grepp.moodlink.app.controller.web.member;

import com.grepp.moodlink.app.controller.web.member.payload.LikeGenreResponse;
import com.grepp.moodlink.app.controller.web.member.payload.ModifyRequest;
import com.grepp.moodlink.app.model.auth.domain.Principal;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.home.HomeService;
import com.grepp.moodlink.app.model.member.MemberService;
import com.grepp.moodlink.app.model.member.dto.MemberInfoDto;
import com.grepp.moodlink.app.model.recomend.LikeService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {

    private final MemberService memberService;
    private final LikeService likeService;
    private final HomeService homeService;


    @GetMapping()
    public String showMyPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken);

        if (!isAuthenticated) {
            return "redirect:/";
        }
        Principal principal = (Principal) authentication.getPrincipal();

        String userId = principal.getUsername();

// 개인 정보 보여주는 로직
        Optional<MemberInfoDto> memberInfo = memberService.GetMemberInfo(userId);

        if (memberInfo.isPresent()) {
            MemberInfoDto info = memberInfo.get();
            model.addAttribute("userId", info.getId());
            model.addAttribute("username", info.getUsername());
            model.addAttribute("createdAt", info.getCreatedAt());
            model.addAttribute("updatedAt", info.getUpdatedAt());
            model.addAttribute("countries", info.getCountries());
        }

        // 좋아하는 장르 보여주는 로직

        List<LikeGenreResponse> likeBookGenre = likeService.getPersonalLikeBookGenre(userId);
        List<LikeGenreResponse> likeMusicGenre = likeService.getPersonalLikeMusicGenre(userId);
        List<LikeGenreResponse> likeMovieGenre = likeService.getPersonalLikeMovieGenre(userId);

        model.addAttribute("LikeBookGenre", likeBookGenre);
        model.addAttribute("LikeMusicGenre", likeMusicGenre);
        model.addAttribute("LikeMovieGenre", likeMovieGenre);

        List<LikeGenreResponse> mostLikeBookGenre = likeService.getMostLikeBookGenre();
        List<LikeGenreResponse> mostLikeMusicGenre = likeService.getMostLikeMusicGenre();
        List<LikeGenreResponse> mostLikeMovieGenre = likeService.getMostLikeMovieGenre();

        model.addAttribute("mostLikeBookGenre", mostLikeBookGenre);
        model.addAttribute("mostLikeMusicGenre", mostLikeMusicGenre);
        model.addAttribute("mostLikeMovieGenre", mostLikeMovieGenre);

        return "/users/mypage";
    }

    @GetMapping("/modify")
    public String showModifyMyPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken);

        if (!isAuthenticated) {
            return "redirect:/";
        }
        Principal principal = (Principal) authentication.getPrincipal();

        String userId = principal.getUsername();
        model.addAttribute("userId", userId);
        model.addAttribute("username", memberService.GetUsername(userId));
        return "/users/modify";

    }

    @PostMapping("/modify")
    public String modifyProfile(ModifyRequest request, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken);

        if (!isAuthenticated) {
            return "redirect:/";
        }
        Principal principal = (Principal) authentication.getPrincipal();

        String userId = principal.getUsername();
        memberService.modifyProfile(userId, request.toDto());
        return "redirect:/users";
    }


    @GetMapping("/like")
    public String showLikePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken);

        if (!isAuthenticated) {
            return "redirect:/";
        }
        Principal principal = (Principal) authentication.getPrincipal();

        String userId = principal.getUsername();

        List<BookDto> likedBooks = likeService.getUserLikedBooks(userId);
        List<MusicDto> likedMusics = likeService.getUserLikedMusics(userId);
        List<MovieInfoDto> likedMovies = likeService.getUserLikedMovies(userId);

        model.addAttribute("likedbooks", likedBooks);
        model.addAttribute("likedmusics", likedMusics);
        model.addAttribute("likedmovies", likedMovies);
        return "users/like";
    }


}
