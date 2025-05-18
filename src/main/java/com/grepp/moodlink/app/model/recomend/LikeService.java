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


    public void updateLikeIncrement(Likes likes){
        Long likeCount = likes.getTotalCount();
        likes.setTotalCount(likeCount+1);
    }

    public Likes createLike(String userId){
        Likes likes1 = new Likes();
        likes1.setTotalCount(0L);
        likes1.setUserId(userId);
        updateLikeIncrement(likes1);
        likeRepository.save(likes1);
        return likes1;
    }

    public void createLikeDetailMusic(Long likeId, String id, Music musicContent){
        // like_detail_musics에 행 추가
        LikeDetailMusic likeDetailMusic1 = new LikeDetailMusic();
        likeDetailMusic1.setLikesId(likeId);
        likeDetailMusic1.setMusicId(id);
        likeDetailMusicRepository.save(likeDetailMusic1);
        // 컨텐츠의 likeCount 값 증가
        musicContent.setLikeCount(musicContent.getLikeCount()+1);
    }

    public void deleteLikeDetailMusic(Likes likes, String id, Music musicContent){
        likeDetailMusicRepository.deleteByMusicIdAndLikesId(id, likes.getId());
        likes.setTotalCount(likes.getTotalCount()-1);
        // 컨텐츠의 likeCount 값 감소
        musicContent.setLikeCount(musicContent.getLikeCount()-1);
    }

    public void createLikeDetailMovie(Long likeId, String id, Movie movieContent){
        // like_detail_movie에 행 추가
        LikeDetailMovies likeDetailMovies1 = new LikeDetailMovies();
        likeDetailMovies1.setLikesId(likeId);
        likeDetailMovies1.setMovieId(id);
        likeDetailMoviesRepository.save(likeDetailMovies1);
        // 컨텐츠의 likeCount 값 증가
        movieContent.setLikeCount(movieContent.getLikeCount()+1);
    }

    public void deleteLikeDetailMovie(Likes likes, String id, Movie movieContent){
        likeDetailMoviesRepository.deleteByMovieIdAndLikesId(id, likes.getId());
        likes.setTotalCount(likes.getTotalCount()-1);
        // 컨텐츠의 likeCount 값 감소
        movieContent.setLikeCount(movieContent.getLikeCount()-1);
    }

    public void createLikeDetailBook(Long likeId, String id, Book bookContent){
        // like_detail_book에 행 추가
        LikeDetailBooks likeDetailBooks1 = new LikeDetailBooks();
        likeDetailBooks1.setLikesId(likeId);
        likeDetailBooks1.setBookId(id);
        likeDetailBooksRepository.save(likeDetailBooks1);
        // 컨텐츠의 likeCount 값 증가
        bookContent.setLikeCount(bookContent.getLikeCount()+1);
    }

    public void deleteLikeDetailBook(Likes likes, String id, Book bookContent){
        likeDetailBooksRepository.deleteByBookIdAndLikesId(id, likes.getId());
        likes.setTotalCount(likes.getTotalCount()-1);
        // 컨텐츠의 likeCount 값 감소
        bookContent.setLikeCount(bookContent.getLikeCount()-1);
    }

    @Transactional
    public boolean toggleLikeMusic(String userId, String id){
        // 좋아요 증감 할 컨텐츠의 likeCount값 조회
        Music musicContent = musicRepository.findById(id).orElseThrow();

        List<Likes> likes = getLikeInfo(userId);
        // 좋아요 목록 없음 -> like 행 추가, likeDetailMusic 행 추가
        if (likes.isEmpty()){
            Likes likes1 = createLike(userId);
            createLikeDetailMusic(likes1.getId(), id, musicContent);
            return true;
        }
        // 좋아요 목록은 있지만, likeDetailMusic에 해당 id의 music이 없음 -> like 행 업데이트, likeDetailMusic 행 추가
        Likes likes1 = likes.getFirst();
        if(!likeDetailMusicRepository.existsByLikesIdAndMusicId(likes1.getId(),id)){
            updateLikeIncrement(likes1);
            createLikeDetailMusic(likes1.getId(), id, musicContent);
            return true;
        }

        // 좋아요 목록이 있고, likeDetailMusic에 해당 id의 music이 있음 -> like 행 업데이트, likeDetailMusic 행 삭제
        deleteLikeDetailMusic(likes1, id, musicContent);

        // like의 totalCount값이 0이면 like테이블에서 삭제
        if (likes1.getTotalCount() == 0){
            likeRepository.deleteById(likes1.getId());
        }
        return false;
    }

    @Transactional
    public boolean toggleLikeMovie(String userId, String id) {
        // 좋아요 증감 할 컨텐츠의 likeCount값 조회
        Movie movieContent = movieRepository.findById(id).orElseThrow();

        List<Likes> likes = getLikeInfo(userId);
        // 좋아요 목록 없음 -> like 행 추가, likeDetailMovie 행 추가
        if (likes.isEmpty()){
            Likes likes1 = createLike(userId);
            createLikeDetailMovie(likes1.getId(), id, movieContent);
            return true;
        }
        // 좋아요 목록은 있지만, likeDetailMovie에 해당 id의 movie이 없음 -> like 행 업데이트, likeDetailMovie 행 추가
        Likes likes1 = likes.getFirst();
        if(!likeDetailMoviesRepository.existsByLikesIdAndMovieId(likes1.getId(),id)){
            updateLikeIncrement(likes1);
            createLikeDetailMovie(likes1.getId(), id, movieContent);
            return true;
        }

        // 좋아요 목록이 있고, likeDetailMovie에 해당 id의 movie이 있음 -> like 행 업데이트, likeDetailMovie 행 삭제
        deleteLikeDetailMovie(likes1, id, movieContent);

        // like의 totalCount값이 0이면 like테이블에서 삭제
        if (likes1.getTotalCount() == 0){
            likeRepository.deleteById(likes1.getId());
        }
        return false;
    }


    @Transactional
    public boolean toggleLikeBook(String userId, String id) {
        // 좋아요 증감 할 컨텐츠의 likeCount값 조회
        Book bookContent = bookRepository.findByIsbn(id);

        List<Likes> likes = getLikeInfo(userId);
        // 좋아요 목록 없음 -> like 행 추가, likeDetailBook 행 추가
        if (likes.isEmpty()){
            Likes likes1 = createLike(userId);
            createLikeDetailBook(likes1.getId(), id, bookContent);
            return true;
        }
        // 좋아요 목록은 있지만, likeDetailBook에 해당 id의 book이 없음 -> like 행 업데이트, likeDetailBook 행 추가
        Likes likes1 = likes.getFirst();
        if(!likeDetailBooksRepository.existsByLikesIdAndBookId(likes1.getId(),id)){
            updateLikeIncrement(likes1);
            createLikeDetailBook(likes1.getId(), id, bookContent);
            return true;
        }

        // 좋아요 목록이 있고, likeDetailBook에 해당 id의 book이 있음 -> like 행 업데이트, likeDetailBook 행 삭제
        deleteLikeDetailBook(likes1, id, bookContent);

        // like의 totalCount값이 0이면 like테이블에서 삭제
        if (likes1.getTotalCount() == 0){
            likeRepository.deleteById(likes1.getId());
        }
        return false;
    }
}