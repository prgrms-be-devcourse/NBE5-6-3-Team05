package com.grepp.moodlink.app.model.result;


import com.grepp.moodlink.app.model.data.book.BookRepository;
import com.grepp.moodlink.app.model.data.movie.MovieRepository;
import com.grepp.moodlink.app.model.data.music.MusicRepository;
import com.grepp.moodlink.app.model.recomend.LikeDetailBooksRepository;
import com.grepp.moodlink.app.model.recomend.LikeDetailMoviesRepository;
import com.grepp.moodlink.app.model.recomend.LikeDetailMusicRepository;
import com.grepp.moodlink.app.model.recomend.LikeRepository;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailBooks;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMovies;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMusic;
import com.grepp.moodlink.app.model.recomend.entity.Likes;
import com.grepp.moodlink.app.model.result.dto.BookDto;
import com.grepp.moodlink.app.model.result.dto.CuratingDetailDto;
import com.grepp.moodlink.app.model.result.dto.MovieDto;
import com.grepp.moodlink.app.model.result.dto.SongDto;
import com.grepp.moodlink.app.model.result.entity.CuratingDetail;
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

    private final String googleStr="https://www.google.com/search?q=";

    public List<CuratingDetailDto> curatingDetailDtoList(String userId){

        // db에서 curating 값 가져오기
        String curatingId = curatingRepository.findByUserId(userId).getFirst().getId();
        List<CuratingDetail> curatingDetails = curatingDetailRepository.findByCuratingId(curatingId);

        String tempBookId;
        String tempSongId;
        String tempMovieId;
        List<CuratingDetailDto> items = new ArrayList<>();

        // db에서 curating의 각 컨텐츠(책, 노래, 영화)들 가져오기
        for(CuratingDetail curatingDetail: curatingDetails) {
            tempBookId = curatingDetail.getBookId();
            tempSongId = curatingDetail.getSongId();
            tempMovieId = curatingDetail.getMovieId();
            BookDto bookDto = bookRepository.findSimpleByIsbn(tempBookId).get();
            SongDto songDto = musicRepository.findSimpleById(tempSongId).get();
            MovieDto movieDto = movieRepository.findSimpleById(tempMovieId).get();
            // 구글 검색으로 외부 검색포탈 주소 set
            bookDto.setExternalLink(googleStr+bookDto.getName());
            songDto.setExternalLink(googleStr+songDto.getName());
            movieDto.setExternalLink(googleStr+movieDto.getName());

            CuratingDetailDto temp1 = new CuratingDetailDto(bookDto, songDto, movieDto);
            items.add(temp1);
        }

        // User가 비회원이면 각 컨텐츠의 상태를 false로 return
        // TODO: 비회원으로 수정해야 함.
        if (userId.equals("anonymous")){
            return items;
        }


        // User의 Like한 목록이 없다면 각 컨텐츠의 상태를 false로 return
        List<Likes> likes = likeRepository.findByUserId(userId);
        if (likes.isEmpty()){
            return items;
        }

        Long likeId;
        LikeDetailMovies likeDetailMovies;
        LikeDetailBooks likeDetailBooks;
        LikeDetailMusic likeDetailMusic;
        for (Likes likes1: likes){
            likeId = likes1.getId();
            likeDetailBooks = likeDetailBooksRepository.findByLikesId(likeId);
            likeDetailMovies = likeDetailMoviesRepository.findByLikesId(likeId);
            likeDetailMusic = likeDetailMusicRepository.findByLikesId(likeId);
            if (likeDetailBooks != null){
                for(CuratingDetailDto item : items){
                    if (item.getBook().getId().equals(likeDetailBooks.getBookId())){
                        item.getBook().setStatus(true);
                    }
                }
            }
            if (likeDetailMovies != null){
                for(CuratingDetailDto item : items){
                    if (item.getMovie().getId().equals(likeDetailMovies.getMovieId())){
                        item.getMovie().setStatus(true);
                    }
                }
            }
            if (likeDetailMusic != null){
                for(CuratingDetailDto item : items){
                    if (item.getSong().getId().equals(likeDetailMusic.getMusicId())){
                        item.getSong().setStatus(true);
                    }
                }
            }
        }


        return items;
    }

}
