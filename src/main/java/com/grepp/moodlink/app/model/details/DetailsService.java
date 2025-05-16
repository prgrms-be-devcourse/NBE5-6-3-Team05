package com.grepp.moodlink.app.model.details;


import com.grepp.moodlink.app.model.data.book.BookRepository;
import com.grepp.moodlink.app.model.data.movie.MovieRepository;
import com.grepp.moodlink.app.model.data.music.MusicRepository;
import com.grepp.moodlink.app.model.details.dto.BookDetailsDto;
import com.grepp.moodlink.app.model.details.dto.MovieDetailsDto;
import com.grepp.moodlink.app.model.details.dto.SongDetailsDto;
import com.grepp.moodlink.app.model.recomend.LikeDetailBooksRepository;
import com.grepp.moodlink.app.model.recomend.LikeDetailMoviesRepository;
import com.grepp.moodlink.app.model.recomend.LikeDetailMusicRepository;
import com.grepp.moodlink.app.model.recomend.LikeRepository;
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
    private final BookRepository bookRepository;
    private final MusicRepository musicRepository;
    private final MovieRepository movieRepository;
    private final LikeRepository likeRepository;
    private final LikeDetailBooksRepository likeDetailBooksRepository;
    private final LikeDetailMusicRepository likeDetailMusicRepository;
    private final LikeDetailMoviesRepository likeDetailMoviesRepository;


    public BookDetailsDto getBookDetails(String userId, String id) {
        BookDetailsDto bookDetailsDto = BookDetailsDto.from(bookRepository.findByIsbn(id));
        bookDetailsDto.setId(id);
        String googleStr="https://www.google.com/search?q=";
        bookDetailsDto.setExternalLink(googleStr+bookDetailsDto.getName());
        bookDetailsDto.setStatus(false);

        // 비회원 시 Like테이블 조회 없이 바로 반환
        if (userId.isEmpty()){
            return bookDetailsDto;
        }

        // User의 Like 목록이 없다면 해당 컨텐츠의 상태를 false로 return
        List<Likes> likes = likeRepository.findByUserId(userId);
        if (likes.isEmpty()){
            return bookDetailsDto;
        }

        return bookDetailsDto;
    }

    public SongDetailsDto getSongDetails(String userId, String id) {
        SongDetailsDto songDetailsDto = SongDetailsDto.from(musicRepository.findById(id).get());
        songDetailsDto.setId(id);
        String googleStr="https://www.google.com/search?q=";
        songDetailsDto.setExternalLink(googleStr+songDetailsDto.getName());
        songDetailsDto.setStatus(false);

        // 비회원 시 Like테이블 조회 없이 바로 반환
        if (userId.isEmpty()){
            return songDetailsDto;
        }

        // User의 Like 목록이 없다면 해당 컨텐츠의 상태를 false로 return
        List<Likes> likes = likeRepository.findByUserId(userId);
        if (likes.isEmpty()){
            return songDetailsDto;
        }

        return songDetailsDto;
    }

    @Transactional
    public MovieDetailsDto getMovieDetails(String userId, String id) {
        MovieDetailsDto movieDetailsDto = MovieDetailsDto.from(movieRepository.findById(id).get());
        movieDetailsDto.setId(id);
        String googleStr="https://www.google.com/search?q=";
        movieDetailsDto.setExternalLink(googleStr+movieDetailsDto.getName());
        movieDetailsDto.setStatus(false);

        // 비회원 시 Like테이블 조회 없이 바로 반환
        if (userId.isEmpty()){
            return movieDetailsDto;
        }

        // User의 Like 목록이 없다면 해당 컨텐츠의 상태를 false로 return
        List<Likes> likes = likeRepository.findByUserId(userId);
        if (likes.isEmpty()){
            return movieDetailsDto;
        }

        return movieDetailsDto;
    }
}
