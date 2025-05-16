package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.controller.web.member.payload.LikeGenreResponse;
import com.grepp.moodlink.app.model.data.book.BookRepository;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.movie.MovieRepository;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.music.MusicRepository;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailBooks;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMovies;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMusic;
import com.grepp.moodlink.app.model.recomend.entity.Likes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {


    private final LikeRepository likeRepository;
    private final LikeDetailMoviesRepository likeDetailMoviesRepository;
    private final LikeDetailBooksRepository likeDetailBooksRepository;
    private final LikeDetailMusicRepository likeDetailMusicRepository;
    private final BookRepository bookRepository;
    private final MusicRepository musicRepository;
    private final MovieRepository movieRepository;
    private final ModelMapper mapper;

    public List<Likes> getLikeInfo(String userId) {
        return likeRepository.findByUserId(userId);
    }

    public List<LikeDetailBooks> getLikeDetailBook(List<Likes> likes) {

        List<String> likeIds = likes.stream()
            .map(like -> String.valueOf(like.getId()))
            .collect(Collectors.toList());
        return likeDetailBooksRepository.findAllByLikesIdIn(likeIds);

    }
    public Page<LikeDetailBooks> getLikeDetailBookPaged(List<Likes> likes,Pageable pageable) {

        List<String> likeIds = likes.stream()
            .map(like -> String.valueOf(like.getId()))
            .collect(Collectors.toList());
        return likeDetailBooksRepository.findAllByLikesIdInPagination(likeIds,pageable);

    }

    public List<BookDto> getBookList(List<LikeDetailBooks> likeDetailBooks) {

        List<String> bookIds = likeDetailBooks.stream()
            .map(LikeDetailBooks::getBookId)
            .collect(Collectors.toList());

        List<Book> books = bookRepository.findAllByIsbnIn(bookIds);

        log.info("Books: {}", books);

        return books.stream()
            .map(BookDto::toDto)
            .collect(Collectors.toList());
    }


    public List<LikeDetailMusic> getLikeDetailMusic(List<Likes> likes) {

        List<String> likeIds = likes.stream()
            .map(like -> String.valueOf(like.getId()))
            .collect(Collectors.toList());
        return likeDetailMusicRepository.findAllByLikesIdIn(likeIds);

    }
    public Page<LikeDetailMusic> getLikeDetailMusicPaged(List<Likes> likes,Pageable pageable) {

        List<String> likeIds = likes.stream()
            .map(like -> String.valueOf(like.getId()))
            .collect(Collectors.toList());
        return likeDetailMusicRepository.findAllByLikesIdInPagination(likeIds,pageable);

    }

    public List<MusicDto> getMusicList(List<LikeDetailMusic> likeDetailMusics) {

        List<String> MusicIds = likeDetailMusics.stream()
            .map(LikeDetailMusic::getMusicId)
            .collect(Collectors.toList());

        List<Music> musics = musicRepository.findAllByIdIn(MusicIds);

        return musics.stream()
            .map(MusicDto::toDto)
            .collect(Collectors.toList());
    }

    public List<LikeDetailMovies> getLikeDetailMovie(List<Likes> likes) {
        List<String> likeIds = likes.stream()
            .map(like -> String.valueOf(like.getId()))
            .collect(Collectors.toList());

        return likeDetailMoviesRepository.findAllByLikesIdIn(likeIds);

    }

    public Page<LikeDetailMovies> getLikeDetailMoviePaged(List<Likes> likes,Pageable pageable) {
        List<String> likeIds = likes.stream()
            .map(like -> String.valueOf(like.getId()))
            .collect(Collectors.toList());

        return likeDetailMoviesRepository.findAllByLikesIdInPagination(likeIds,pageable);

    }

    public List<MovieInfoDto> getMovieList(List<LikeDetailMovies> likeDetailMovies) {
        List<String> MovieIds = likeDetailMovies.stream()
            .map(LikeDetailMovies::getMovieId)
            .collect(Collectors.toList());

        List<Movie> movies = movieRepository.findAllByIdIn(MovieIds);

        return movies.stream()
            .map(MovieInfoDto::toDto)
            .collect(Collectors.toList());
    }


    @Transactional
    public List<MusicDto> getUserLikedMusics(String userId) {
        List<Likes> likes = getLikeInfo(userId);
        List<LikeDetailMusic> likeDetailMusic = getLikeDetailMusic(likes);
        return getMusicList(likeDetailMusic);
    }

    @Transactional
    public List<BookDto> getUserLikedBooks(String userId) {
        List<Likes> likes = getLikeInfo(userId);
        log.info("likes: {}", likes);
        List<LikeDetailBooks> likeDetailBooks = getLikeDetailBook(likes);
        log.info("likedetailBooks: {}", likeDetailBooks);
        return getBookList(likeDetailBooks);
    }

    @Transactional
    public List<MovieInfoDto> getUserLikedMovies(String userId) {
        List<Likes> likes = getLikeInfo(userId);
        List<LikeDetailMovies> likeDetailMovies = getLikeDetailMovie(likes);
        return getMovieList(likeDetailMovies);
    }

    @Transactional
    public List<LikeGenreResponse> getPersonalLikeMovieGenre(String userId) {
        List<MovieInfoDto> movies = getUserLikedMovies(userId);

        Map<String, Long> genreCount = new HashMap<>();

        for (MovieInfoDto movie : movies) {
            Set<Genre> genres = movie.getGenres();
            for (Genre genre : genres) {
                String genreName = genre.getName();
                if (genreName != null && !genreName.isBlank()) {
                    genreCount.put(genreName, genreCount.getOrDefault(genreName, 0L) + 1);
                }
            }
        }

        return genreCount.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(6)
            .map(entry -> new LikeGenreResponse(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Transactional
    public List<LikeGenreResponse> getMostLikeMovieGenre() {

        List<Movie> movies = movieRepository.findAll();
        Map<String, Long> genreCount = new HashMap<>();

        for (Movie movie : movies) {
            Set<Genre> genres = movie.getGenres();
            Long likeCount = movie.getLikeCount();
            for (Genre genre : genres) {
                String genreName = genre.getName();
                if (genreName != null && !genreName.isBlank() && likeCount != null) {
                    genreCount.put(genreName, genreCount.getOrDefault(genreName, 0L) + likeCount);
                }
            }
        }

        return genreCount.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(6)
            .map(entry -> new LikeGenreResponse(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }


    @Transactional
    public List<LikeGenreResponse> getPersonalLikeMusicGenre(String userId) {
        List<MusicDto> musics = getUserLikedMusics(userId);

        Map<String, Long> genreCount = new HashMap<>();

        for (MusicDto music : musics) {
            String genre = music.getGenre();
            if (genre != null && !genre.isBlank()) {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0L) + 1);
            }
        }

        return genreCount.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(6)
            .map(entry -> new LikeGenreResponse(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Transactional
    public List<LikeGenreResponse> getMostLikeMusicGenre() {

        List<Music> musics = musicRepository.findAll();
        Map<String, Long> genreCount = new HashMap<>();

        for (Music music : musics) {
            String genre = music.getGenre();
            Long likeCount = music.getLikeCount();
            if (genre != null && !genre.isBlank() && likeCount != null) {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0L) + likeCount);
            }
        }

        return genreCount.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(6)
            .map(entry -> new LikeGenreResponse(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }


    @Transactional
    public List<LikeGenreResponse> getPersonalLikeBookGenre(String userId) {
        List<BookDto> books = getUserLikedBooks(userId);

        Map<String, Long> genreCount = new HashMap<>();

        for (BookDto book : books) {
            String genre = book.getGenre();
            if (genre != null && !genre.isBlank()) {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0L) + 1);
            }
        }

        return genreCount.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(6)
            .map(entry -> new LikeGenreResponse(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Transactional
    public List<LikeGenreResponse> getMostLikeBookGenre() {

        List<Book> books = bookRepository.findAll();
        Map<String, Long> genreCount = new HashMap<>();

        for (Book book : books) {
            String genre = book.getGenre();
            Long likeCount = book.getLikeCount();
            if (genre != null && !genre.isBlank() && likeCount != null) {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0L) + likeCount);
            }
        }

        return genreCount.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(6)
            .map(entry -> new LikeGenreResponse(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }


    //TODO: like테이블 수정 및 각 컨텐츠의 like_count증감 적용
    @Transactional
    public Page<BookDto> getUserLikedBooksPaged(String userId, Pageable pageable) {
        List<Likes> likes = getLikeInfo(userId);
        Page<LikeDetailBooks> likeDetailBooks = getLikeDetailBookPaged(likes,pageable);
        List<BookDto> Books = getBookList(likeDetailBooks.getContent());

        return new PageImpl<>(Books, pageable, likeDetailBooks.getTotalElements());
    }

    @Transactional
    public Page<MusicDto> getUserLikedMusicsPaged(String userId, Pageable pageable) {
        List<Likes> likes = getLikeInfo(userId);
        Page<LikeDetailMusic> likeDetailMusics = getLikeDetailMusicPaged(likes,pageable);
        List<MusicDto> Musics = getMusicList(likeDetailMusics.getContent());

        return new PageImpl<>(Musics, pageable, likeDetailMusics.getTotalElements());
    }@Transactional
    public Page<MovieInfoDto> getUserLikedMoviesPaged(String userId, Pageable pageable) {
        List<Likes> likes = getLikeInfo(userId);
        Page<LikeDetailMovies> likeDetailMovies = getLikeDetailMoviePaged(likes,pageable);
        List<MovieInfoDto> Movies = getMovieList(likeDetailMovies.getContent());

        return new PageImpl<>(Movies, pageable, likeDetailMovies.getTotalElements());
    }


    @Transactional
    public boolean toggleLikeMusic(String userId, String id){
        List<Likes> likes = getLikeInfo(userId);
        List<LikeDetailMusic> likeDetailMusic = getLikeDetailMusic(likes);
        boolean exists = true;
        for(LikeDetailMusic music : likeDetailMusic){
            if(music.getMusicId().equals(id)){
                exists = false;

                likeDetailMusicRepository.deleteByMusicId(id);

                // likes 테이블에 요소 삭제
                likeRepository.deleteById(music.getLikesId());
                // like_detail_musics 테이블에 요소 삭제
                // 삭제
                break;
            }
        }
        if (exists){
            // 삽입하기
            // likes테이블에 요소 추가
            Likes newLike = new Likes();
            newLike.setUserId(userId);
            likeRepository.save(newLike);

            // like_detail_musics 테이블에 요소 추가
            LikeDetailMusic newLikeMusic = new LikeDetailMusic();
            newLikeMusic.setMusicId(id);
            newLikeMusic.setLikesId(newLike.getId());
            likeDetailMusicRepository.save(newLikeMusic);
        }
        return exists;
    }

    @Transactional
    public boolean toggleLikeMovie(String userId, String id) {
        List<Likes> likes = getLikeInfo(userId);
        List<LikeDetailMovies> likeDetailMovies = getLikeDetailMovie(likes);
        boolean exists = true;
        for(LikeDetailMovies movie : likeDetailMovies){
            if(movie.getMovieId().equals(id)){
                exists = false;
                // like_detail_movie 테이블에 요소 삭제
                likeDetailMoviesRepository.deleteByMovieId(id);
                // 삭제

                // likes 테이블에 요소 삭제
                likeRepository.deleteById(movie.getLikesId());

                break;
            }
        }
        if (exists){
            // 삽입하기
            // likes테이블에 요소 추가
            Likes newLike = new Likes();
            newLike.setUserId(userId);
            likeRepository.save(newLike);

            // like_detail_movie 테이블에 요소 추가
            LikeDetailMovies newLikeMovies = new LikeDetailMovies();
            newLikeMovies.setMovieId(id);
            newLikeMovies.setLikesId(newLike.getId());
            likeDetailMoviesRepository.save(newLikeMovies);
        }
        return exists;
    }

    @Transactional
    public boolean toggleLikeBook(String userId, String id) {
        List<Likes> likes = getLikeInfo(userId);
        List<LikeDetailBooks> likeDetailBooks = getLikeDetailBook(likes);
        boolean exists = true;
        for(LikeDetailBooks book : likeDetailBooks){
            if(book.getBookId().equals(id)){
                exists = false;
                // like_detail_book 테이블에 요소 삭제
                likeDetailBooksRepository.deleteByBookId(id);
                // 삭제
                // likes 테이블에 요소 삭제
                likeRepository.deleteById(book.getLikesId());

                break;
            }
        }
        if (exists){
            // 삽입하기
            // likes테이블에 요소 추가
            Likes newLike = new Likes();
            newLike.setUserId(userId);
            likeRepository.save(newLike);

            // like_detail_book 테이블에 요소 추가
            LikeDetailBooks newLikeBooks = new LikeDetailBooks();
            newLikeBooks.setBookId(id);
            newLikeBooks.setLikesId(newLike.getId());
            likeDetailBooksRepository.save(newLikeBooks);
        }
        return exists;
    }
}



