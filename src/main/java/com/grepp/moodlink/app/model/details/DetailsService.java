package com.grepp.moodlink.app.model.details;


import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.movie.MovieService;
import com.grepp.moodlink.app.model.data.music.MusicService;
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


    public BookDetailsDto getBookDetails(String userId, String id) {
        BookDetailsDto bookDetailsDto = BookDetailsDto.from(bookService.findByIsbn(id));
        // 비회원 시 Like테이블 조회 없이 바로 반환
        if (userId.isEmpty()){
            return bookDetailsDto;
        }

        // User의 Like 목록이 없다면 해당 컨텐츠의 상태를 false로 return
        List<Likes> likes = likeService.getLikeInfo(userId);
        if (likes.isEmpty()){
            return bookDetailsDto;
        }

        // Like테이블 조회 후 현재 상세정보를 확인하는 컨텐츠와 같은 id의 컨텐츠가 있으면 status를 true로 변환
        if(likeService.existInLikeDetailBook(likes.getFirst().getId(),id)){
            bookDetailsDto.setStatus(true);
        }
        System.out.println(bookDetailsDto);
        return bookDetailsDto;
    }

    public SongDetailsDto getSongDetails(String userId, String id) {
        SongDetailsDto songDetailsDto = SongDetailsDto.from(musicService.findById(id));

        // 비회원 시 Like테이블 조회 없이 바로 반환
        if (userId.isEmpty()){
            return songDetailsDto;
        }

        // User의 Like 목록이 없다면 해당 컨텐츠의 상태를 false로 return
        List<Likes> likes = likeService.getLikeInfo(userId);
        if (likes.isEmpty()){
            return songDetailsDto;
        }

        // Like테이블 조회 후 현재 상세정보를 확인하는 컨텐츠와 같은 id의 컨텐츠가 있으면 status를 true로 변환
        if(likeService.existInLikeDetailMusic(likes.getFirst().getId(),id)){
            songDetailsDto.setStatus(true);
        }

        return songDetailsDto;
    }

    @Transactional
    public MovieDetailsDto getMovieDetails(String userId, String id) {
        MovieDetailsDto movieDetailsDto = MovieDetailsDto.from(movieService.findById(id));

        // 비회원 시 Like테이블 조회 없이 바로 반환
        if (userId.isEmpty()){
            return movieDetailsDto;
        }

        // User의 Like 목록이 없다면 해당 컨텐츠의 상태를 false로 return
        List<Likes> likes = likeService.getLikeInfo(userId);
        if (likes.isEmpty()){
            return movieDetailsDto;
        }

        // Like테이블 조회 후 현재 상세정보를 확인하는 컨텐츠와 같은 id의 컨텐츠가 있으면 status를 true로 변환
        if(likeService.existInLikeDetailMovie(likes.getFirst().getId(),id)){
            movieDetailsDto.setStatus(true);
        }

        return movieDetailsDto;
    }
}
