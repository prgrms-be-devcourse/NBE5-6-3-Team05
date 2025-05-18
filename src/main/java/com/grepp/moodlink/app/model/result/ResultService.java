package com.grepp.moodlink.app.model.result;


import com.grepp.moodlink.app.model.data.book.BookRepository;
import com.grepp.moodlink.app.model.data.movie.MovieRepository;
import com.grepp.moodlink.app.model.data.music.MusicRepository;
import com.grepp.moodlink.app.model.recomend.LikeDetailBooksRepository;
import com.grepp.moodlink.app.model.recomend.LikeDetailMoviesRepository;
import com.grepp.moodlink.app.model.recomend.LikeDetailMusicRepository;
import com.grepp.moodlink.app.model.recomend.LikeRepository;
import com.grepp.moodlink.app.model.recomend.entity.Likes;
import com.grepp.moodlink.app.model.result.dto.BookDto;
import com.grepp.moodlink.app.model.result.dto.CuratingDetailDto;
import com.grepp.moodlink.app.model.result.dto.CuratingDetailIdDto;
import com.grepp.moodlink.app.model.result.dto.MovieDto;
import com.grepp.moodlink.app.model.result.dto.SongDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultService {
    private final CuratingRepository curatingRepository;
    private final CuratingDetailRepository curatingDetailRepository;
    private final BookRepository bookRepository;
    private final MovieRepository movieRepository;
    private final MusicRepository musicRepository;
    private final LikeRepository likeRepository;
    private final LikeDetailMusicRepository likeDetailMusicRepository;
    private final LikeDetailBooksRepository likeDetailBooksRepository;
    private final LikeDetailMoviesRepository likeDetailMoviesRepository;


    public List<CuratingDetailDto> curatingDetailDtoList(String userId, List<CuratingDetailIdDto> recommendResult){
        // db에서 curating 값 가져오기
        List<CuratingDetailDto> items = getItems(recommendResult);

        // User가 비회원이면 각 컨텐츠의 상태를 false로 return
        if (userId.isEmpty()){
            return items;
        }

        // User의 Like한 목록이 없다면 각 컨텐츠의 상태를 false로 return
        List<Likes> likes = likeRepository.findByUserId(userId);
        if (likes.isEmpty()){
            return items;
        }

        // user가 좋아요를 누른 컨텐츠가 하나 이상일 경우 curating 결과와 교집합이 있는지 확인
        updateStatus(likes, items);

        // curatingDto 저장해야 함..
        return items;
    }

    public List<CuratingDetailDto> getItems(List<CuratingDetailIdDto> recommendResult){
        List<CuratingDetailDto> items = new ArrayList<>();

        // db에서 curating의 각 컨텐츠(책, 노래, 영화)들 가져오기
        for(CuratingDetailIdDto curatingDetail: recommendResult) {
            String tempBookId = curatingDetail.getBookId();
            String tempSongId = curatingDetail.getSongId();
            String tempMovieId = curatingDetail.getMovieId();

            BookDto bookDto = BookDto.from(bookRepository.findByIsbn(tempBookId));
            MovieDto movieDto = MovieDto.from(movieRepository.findById(tempMovieId).orElseThrow());
            SongDto songDto = SongDto.from(musicRepository.findById(tempSongId).orElseThrow());

            items.add(new CuratingDetailDto(bookDto, songDto, movieDto));
        }
        return items;
    }

    public void updateStatus(List<Likes> likes, List<CuratingDetailDto> items){
        Long likeId = likes.getFirst().getId();

        // 좋아요 누른 music에 대해 true로 상태변경
        for(CuratingDetailDto curatingDetailDto: items){
            if(likeDetailBooksRepository.existsByLikesIdAndBookId(likeId, curatingDetailDto.getBook().getId())){
                curatingDetailDto.getBook().setStatus(true);
            }
            if(likeDetailMusicRepository.existsByLikesIdAndMusicId(likeId, curatingDetailDto.getSong().getId())){
                curatingDetailDto.getSong().setStatus(true);
            }
            if(likeDetailMoviesRepository.existsByLikesIdAndMovieId(likeId, curatingDetailDto.getMovie().getId())){
                curatingDetailDto.getMovie().setStatus(true);
            }
        }
    }

}
