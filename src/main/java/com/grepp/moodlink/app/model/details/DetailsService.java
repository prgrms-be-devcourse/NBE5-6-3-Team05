package com.grepp.moodlink.app.model.details;


import com.grepp.moodlink.app.controller.api.details.payload.BookDetailResponse;
import com.grepp.moodlink.app.controller.api.details.payload.MovieDetailResponse;
import com.grepp.moodlink.app.controller.api.details.payload.MusicDetailResponse;
import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.book.dto.BookGenreDto;
import com.grepp.moodlink.app.model.data.movie.MovieService;
import com.grepp.moodlink.app.model.data.movie.dto.GenreDto;
import com.grepp.moodlink.app.model.data.music.MusicService;
import com.grepp.moodlink.app.model.data.music.dto.MusicGenreDto;
import com.grepp.moodlink.app.model.details.dto.BookDetailsDto;
import com.grepp.moodlink.app.model.details.dto.MovieDetailsDto;
import com.grepp.moodlink.app.model.details.dto.SongDetailsDto;
import com.grepp.moodlink.app.model.recomend.LikeService;
import com.grepp.moodlink.app.model.recomend.entity.Likes;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DetailsService {

    // db에서 가져오자. 현재는 likeService 내부의 like상태를 가져와 쓰는중
    private final MusicService musicService;
    private final BookService bookService;
    private final MovieService movieService;
    private final LikeService likeService;


    @Transactional
    public BookDetailResponse getBookDetails(String userId, String id) {
        BookDetailsDto bookDetailsDto = BookDetailsDto.from(bookService.findByIsbn(id));
        BookGenreDto bookGenreDto = bookService.getBookGenre(id);
        // 비회원 시 Like테이블 조회 없이 바로 반환
        if (userId.isEmpty()) {
            return new BookDetailResponse(bookDetailsDto,bookGenreDto);
        }

        // User의 Like 목록이 없다면 해당 컨텐츠의 상태를 false로 return
        List<Likes> likes = likeService.getLikeInfo(userId);
        if (likes.isEmpty()) {
            return new BookDetailResponse(bookDetailsDto,bookGenreDto);
        }

        // Like테이블 조회 후 현재 상세정보를 확인하는 컨텐츠와 같은 id의 컨텐츠가 있으면 status를 true로 변환
        if (likeService.existInLikeDetailBook(likes.getFirst().getId(), id)) {
            bookDetailsDto.setStatus(true);
        }
        System.out.println(bookDetailsDto);

        return new BookDetailResponse(bookDetailsDto,bookGenreDto);
    }




@Transactional
    public MusicDetailResponse getSongDetails(String userId, String id) {
        SongDetailsDto songDetailsDto = SongDetailsDto.from(musicService.findById(id));
    MusicGenreDto musicGenreDto = musicService.getMusicGenre(id);

        // 비회원 시 Like테이블 조회 없이 바로 반환
        if (userId.isEmpty()) {
            return new MusicDetailResponse(songDetailsDto, musicService.getMusicGenre(id));
        }

        // User의 Like 목록이 없다면 해당 컨텐츠의 상태를 false로 return
        List<Likes> likes = likeService.getLikeInfo(userId);
        if (likes.isEmpty()) {
            return new MusicDetailResponse(songDetailsDto, musicService.getMusicGenre(id));
        }

        // Like테이블 조회 후 현재 상세정보를 확인하는 컨텐츠와 같은 id의 컨텐츠가 있으면 status를 true로 변환
        if (likeService.existInLikeDetailMusic(likes.getFirst().getId(), id)) {
            songDetailsDto.setStatus(true);
        }


        return new MusicDetailResponse(songDetailsDto, musicService.getMusicGenre(id));
    }

    @Transactional
    public MovieDetailResponse getMovieDetails(String userId, String id) {
        MovieDetailsDto movieDetailsDto = MovieDetailsDto.from(movieService.findById(id));
        List<GenreDto> movieGenreDto =movieService.getMovieGenre(id);

        // 비회원 시 Like테이블 조회 없이 바로 반환
        if (userId.isEmpty()) {
            return new MovieDetailResponse(movieDetailsDto,movieGenreDto);
        }

        // User의 Like 목록이 없다면 해당 컨텐츠의 상태를 false로 return
        List<Likes> likes = likeService.getLikeInfo(userId);
        if (likes.isEmpty()) {
            return new MovieDetailResponse(movieDetailsDto,movieGenreDto);
        }

        // Like테이블 조회 후 현재 상세정보를 확인하는 컨텐츠와 같은 id의 컨텐츠가 있으면 status를 true로 변환
        if (likeService.existInLikeDetailMovie(likes.getFirst().getId(), id)) {
            movieDetailsDto.setStatus(true);
        }

        return new MovieDetailResponse(movieDetailsDto,movieGenreDto);
    }



}
