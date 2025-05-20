package com.grepp.moodlink.app.controller.web.member;

import com.grepp.moodlink.app.controller.web.member.payload.LikeGenreResponse;
import com.grepp.moodlink.app.controller.web.member.payload.ModifyRequest;
import com.grepp.moodlink.app.model.auth.domain.Principal;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.home.HomeService;
import com.grepp.moodlink.app.model.member.MemberService;
import com.grepp.moodlink.app.model.member.dto.MemberInfoDto;
import com.grepp.moodlink.app.model.recomend.LikeService;
import com.grepp.moodlink.infra.response.PageResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            model.addAttribute("periods", info.getPeriods());
            model.addAttribute("genre", info.getGenre());

        }

        // 좋아하는 장르 보여주는 로직

        List<LikeGenreResponse> likeBookGenre = likeService.getPersonalLikeBookGenre(userId);
        List<LikeGenreResponse> likeMusicGenre = likeService.getPersonalLikeMusicGenre(userId);
        List<LikeGenreResponse> likeMovieGenre = likeService.getPersonalLikeMovieGenre(userId);


        log.info("영화 좋아요 수: {}", likeMovieGenre.size());


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
        Optional<MemberInfoDto> memberInfo = memberService.GetMemberInfo(userId);

        if (memberInfo.isPresent()) {
            MemberInfoDto info = memberInfo.get();
            model.addAttribute("userId", info.getId());
            model.addAttribute("username", info.getUsername());
            model.addAttribute("countries", info.getCountries());
            model.addAttribute("periods", info.getPeriods());
            model.addAttribute("genre", info.getGenre());

        }

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
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        try {
            memberService.modifyProfile(userId, request.toDto());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("userId", userId);
            return "users/modify";
        }
        return "redirect:/users";
    }


    @GetMapping("/like")
    public String showLikePage(Model model,
        @RequestParam(value = "book_page", defaultValue = "0") int bookPage,
        @RequestParam(value = "music_page", defaultValue = "0") int musicPage,
        @RequestParam(value = "movie_page", defaultValue = "0") int moviePage,
        @RequestParam(value = "size", defaultValue = "6") int size,
        @RequestParam(value = "tab", defaultValue = "books") String tab) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken);

        if (!isAuthenticated) {
            return "redirect:/";
        }
        Principal principal = (Principal) authentication.getPrincipal();

        String userId = principal.getUsername();

        Pageable bookPageable = PageRequest.of(bookPage, size,Sort.by(Sort.Direction.DESC, "createdAt"));
        Pageable musicPageable = PageRequest.of(musicPage, size,Sort.by(Sort.Direction.DESC, "createdAt"));
        Pageable moviePageable = PageRequest.of(moviePage, size,Sort.by(Sort.Direction.DESC, "createdAt"));

        System.out.println("Book Page: " + bookPage);
        System.out.println("북페이지 크기: " + size);
        System.out.println("Music Page: " + musicPage);
        System.out.println("뮤직페이지 크기: " + size);
        System.out.println("Movie Page: " + moviePage);

        Page<BookDto> likedBooksPage = likeService.getUserLikedBooksPaged(userId, bookPageable);
        Page<MusicDto> likedMusicsPage = likeService.getUserLikedMusicsPaged(userId, musicPageable);
        Page<MovieInfoDto> likedMoviesPage = likeService.getUserLikedMoviesPaged(userId, moviePageable);



        System.out.println("총 도서 좋아요 수: " + likedBooksPage.getTotalElements());
        System.out.println("총 페이지 수: " + likedBooksPage.getTotalPages());
        System.out.println("총 영화 좋아요 수: " + likedMoviesPage.getTotalElements());
        System.out.println("총 페이지 수: " + likedMoviesPage.getTotalPages());
        System.out.println("총 음악 좋아요 수: " + likedMusicsPage.getTotalElements());
        System.out.println("총 페이지 수: " + likedMusicsPage.getTotalPages());



        PageResponse<BookDto> bookResponse = new PageResponse<>("users/like", likedBooksPage, 5);
        PageResponse<MusicDto> musicResponse = new PageResponse<>("users/like", likedMusicsPage, 5);
        PageResponse<MovieInfoDto> movieResponse = new PageResponse<>("users/like", likedMoviesPage, 5);

        model.addAttribute("likedbooks", bookResponse);
        model.addAttribute("likedmusics", musicResponse);
        model.addAttribute("likedmovies", movieResponse);
        model.addAttribute("activeTab", tab);
        return "users/like";
    }


}
