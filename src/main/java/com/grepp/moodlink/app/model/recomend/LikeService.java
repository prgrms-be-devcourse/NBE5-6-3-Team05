package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.controller.web.member.payload.LikeGenreResponse;
import com.grepp.moodlink.app.model.data.book.BookRepository;
import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.movie.MovieRepository;
import com.grepp.moodlink.app.model.data.movie.MovieService;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.music.MusicRepository;
import com.grepp.moodlink.app.model.data.music.MusicService;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailBooks;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMovies;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMusic;
import com.grepp.moodlink.app.model.recomend.entity.Likes;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final BookService bookService;
    private final MovieService movieService;
    private final MusicService musicService;


    public List<Likes> getLikeInfo(String userId) {
        return likeRepository.findByUserId(userId);
    }

    public List<LikeDetailBooks> getLikeDetailBook(List<Likes> likes) {

        List<Long> likeIds = likes.stream()
            .map(Likes::getId)
            .collect(Collectors.toList());
        return likeDetailBooksRepository.findAllByLikesIdIn(likeIds);

    }

    public Page<LikeDetailBooks> getLikeDetailBookPaged(List<Likes> likes, Pageable pageable) {
        List<Long> likeIds = likes.stream()
            .map(Likes::getId)
            .collect(Collectors.toList());
        return likeDetailBooksRepository.findAllByLikesIdInPagination(likeIds, pageable);

    }

    public List<BookDto> getBookList(List<LikeDetailBooks> likeDetailBooks) {
        List<String> bookIds = likeDetailBooks.stream()
            .map(LikeDetailBooks::getBookId)
            .collect(Collectors.toList());

        List<Book> books = bookRepository.findAllByIsbnIn(bookIds);

        Map<String, Book> bookMap = books.stream()
            .collect(Collectors.toMap(Book::getIsbn, b -> b));

        return bookIds.stream()
            .map(bookMap::get)
            .filter(Objects::nonNull)
            .map(BookDto::toDto)
            .collect(Collectors.toList());
    }


    public List<LikeDetailMusic> getLikeDetailMusic(List<Likes> likes) {

        List<Long> likeIds = likes.stream()
            .map(Likes::getId)
            .collect(Collectors.toList());
        return likeDetailMusicRepository.findAllByLikesIdIn(likeIds);

    }

    public Page<LikeDetailMusic> getLikeDetailMusicPaged(List<Likes> likes, Pageable pageable) {
        List<Long> likeIds = likes.stream()
            .map(Likes::getId)
            .collect(Collectors.toList());
        return likeDetailMusicRepository.findAllByLikesIdInPagination(likeIds, pageable);

    }

    public List<MusicDto> getMusicList(List<LikeDetailMusic> likeDetailMusics) {
        List<String> musicIds = likeDetailMusics.stream()
            .map(LikeDetailMusic::getMusicId)
            .collect(Collectors.toList());

        List<Music> musics = musicRepository.findAllByIdIn(musicIds);

        Map<String, Music> musicMap = musics.stream()
            .collect(Collectors.toMap(Music::getId, m -> m));

        return musicIds.stream()
            .map(musicMap::get)
            .filter(Objects::nonNull)
            .map(MusicDto::toDto)
            .collect(Collectors.toList());
    }


    public List<LikeDetailMovies> getLikeDetailMovie(List<Likes> likes) {
        List<Long> likeIds = likes.stream()
            .map((Likes::getId))
            .collect(Collectors.toList());

        return likeDetailMoviesRepository.findAllByLikesIdIn(likeIds);

    }

    public Page<LikeDetailMovies> getLikeDetailMoviePaged(List<Likes> likes, Pageable pageable) {
        List<Long> likeIds = likes.stream()
            .map((Likes::getId))
            .collect(Collectors.toList());

        return likeDetailMoviesRepository.findAllByLikesIdInPagination(likeIds, pageable);

    }

    public List<MovieInfoDto> getMovieList(List<LikeDetailMovies> likeDetailMovies) {
        List<String> movieIds = likeDetailMovies.stream()
            .map(LikeDetailMovies::getMovieId)
            .collect(Collectors.toList());

        List<Movie> movies = movieRepository.findAllByIdIn(movieIds);

        Map<String, Movie> movieMap = movies.stream()
            .collect(Collectors.toMap(Movie::getId, m -> m));

        return movieIds.stream()
            .map(movieMap::get)
            .filter(Objects::nonNull)
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
        List<LikeDetailBooks> likeDetailBooks = getLikeDetailBook(likes);
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
        Page<LikeDetailBooks> likeDetailBooks = getLikeDetailBookPaged(likes, pageable);

        System.out.println("좋아요 세부 정보 개수: " + likeDetailBooks.getContent());
        List<BookDto> Books = getBookList(likeDetailBooks.getContent());
        System.out.println("북리스트 개수: " + Books.size());

        return new PageImpl<>(Books, pageable, likeDetailBooks.getTotalElements());
    }

    @Transactional
    public Page<MusicDto> getUserLikedMusicsPaged(String userId, Pageable pageable) {
        List<Likes> likes = getLikeInfo(userId);
        Page<LikeDetailMusic> likeDetailMusics = getLikeDetailMusicPaged(likes, pageable);
        List<MusicDto> Musics = getMusicList(likeDetailMusics.getContent());

        return new PageImpl<>(Musics, pageable, likeDetailMusics.getTotalElements());
    }

    @Transactional
    public Page<MovieInfoDto> getUserLikedMoviesPaged(String userId, Pageable pageable) {
        List<Likes> likes = getLikeInfo(userId);
        Page<LikeDetailMovies> likeDetailMovies = getLikeDetailMoviePaged(likes, pageable);
        List<MovieInfoDto> Movies = getMovieList(likeDetailMovies.getContent());

        return new PageImpl<>(Movies, pageable, likeDetailMovies.getTotalElements());
    }


    public void updateLikeIncrement(Likes likes) {
        Long likeCount = likes.getTotalCount();
        likes.setTotalCount(likeCount + 1);
    }

    public Likes createLike(String userId) {
        Likes likes1 = new Likes();
        likes1.setTotalCount(0L);
        likes1.setUserId(userId);
        updateLikeIncrement(likes1);
        likeRepository.save(likes1);
        return likes1;
    }

    public void createLikeDetailMusic(Long likeId, String id) {
        // like_detail_musics에 행 추가
        LikeDetailMusic likeDetailMusic1 = new LikeDetailMusic();
        likeDetailMusic1.setLikesId(likeId);
        likeDetailMusic1.setMusicId(id);
        likeDetailMusic1.setCreatedAt(LocalDateTime.now());
        likeDetailMusicRepository.save(likeDetailMusic1);
        // 컨텐츠의 likeCount 값 증가
        musicService.incrementLikeCount(id);
    }

    public void deleteLikeDetailMusic(Likes likes, String id) {
        likeDetailMusicRepository.deleteByMusicIdAndLikesId(id, likes.getId());
        likes.setTotalCount(likes.getTotalCount() - 1);
        // 컨텐츠의 likeCount 값 감소
        musicService.decreaseLikeCount(id);
    }

    public void createLikeDetailMovie(Long likeId, String id) {
        // like_detail_movie에 행 추가
        LikeDetailMovies likeDetailMovies1 = new LikeDetailMovies();
        likeDetailMovies1.setLikesId(likeId);
        likeDetailMovies1.setMovieId(id);
        likeDetailMovies1.setCreatedAt(LocalDateTime.now());
        likeDetailMoviesRepository.save(likeDetailMovies1);
        // 컨텐츠의 likeCount 값 증가
        movieService.incrementLikeCount(id);
    }

    public void deleteLikeDetailMovie(Likes likes, String id) {
        likeDetailMoviesRepository.deleteByMovieIdAndLikesId(id, likes.getId());
        likes.setTotalCount(likes.getTotalCount() - 1);
        // 컨텐츠의 likeCount 값 감소
        movieService.decreaseLikeCount(id);
    }

    public void createLikeDetailBook(Long likeId, String id) {
        // like_detail_book에 행 추가
        LikeDetailBooks likeDetailBooks1 = new LikeDetailBooks();
        likeDetailBooks1.setLikesId(likeId);
        likeDetailBooks1.setBookId(id);
        likeDetailBooks1.setCreatedAt(LocalDateTime.now());
        likeDetailBooksRepository.save(likeDetailBooks1);
        // 컨텐츠의 likeCount 값 증가
        bookService.incrementLikeCount(id);
    }

    public void deleteLikeDetailBook(Likes likes, String id) {
        likeDetailBooksRepository.deleteByBookIdAndLikesId(id, likes.getId());
        likes.setTotalCount(likes.getTotalCount() - 1);
        // 컨텐츠의 likeCount 값 감소
        bookService.decreaseLikeCount(id);
    }

    public boolean existInLikeDetailMusic(Long likeId, String id) {
        return likeDetailMusicRepository.existsByLikesIdAndMusicId(likeId, id);
    }

    public boolean existInLikeDetailMovie(Long likeId, String id) {
        return likeDetailMoviesRepository.existsByLikesIdAndMovieId(likeId, id);
    }

    public boolean existInLikeDetailBook(Long likeId, String id) {
        return likeDetailBooksRepository.existsByLikesIdAndBookId(likeId, id);
    }

    @Transactional
    public boolean toggleLikeMusic(String userId, String id) {
        List<Likes> likes = getLikeInfo(userId);
        // 좋아요 목록 없음 -> like 행 추가, likeDetailMusic 행 추가
        if (likes.isEmpty()) {
            Likes likes1 = createLike(userId);
            createLikeDetailMusic(likes1.getId(), id);
            return true;
        }
        // 좋아요 목록은 있지만, likeDetailMusic에 해당 id의 music이 없음 -> like 행 업데이트, likeDetailMusic 행 추가
        Likes likes1 = likes.getFirst();
        if (!existInLikeDetailMusic(likes1.getId(), id)) {
            updateLikeIncrement(likes1);
            createLikeDetailMusic(likes1.getId(), id);
            return true;
        }

        // 좋아요 목록이 있고, likeDetailMusic에 해당 id의 music이 있음 -> like 행 업데이트, likeDetailMusic 행 삭제
        deleteLikeDetailMusic(likes1, id);

        // like의 totalCount값이 0이면 like테이블에서 삭제
        if (likes1.getTotalCount() == 0) {
            likeRepository.deleteById(likes1.getId());
        }
        return false;
    }

    @Transactional
    public boolean toggleLikeMovie(String userId, String id) {
        List<Likes> likes = getLikeInfo(userId);
        // 좋아요 목록 없음 -> like 행 추가, likeDetailMovie 행 추가
        if (likes.isEmpty()) {
            Likes likes1 = createLike(userId);
            createLikeDetailMovie(likes1.getId(), id);
            return true;
        }
        // 좋아요 목록은 있지만, likeDetailMovie에 해당 id의 movie이 없음 -> like 행 업데이트, likeDetailMovie 행 추가
        Likes likes1 = likes.getFirst();
        if (!existInLikeDetailMovie(likes1.getId(), id)) {
            updateLikeIncrement(likes1);
            createLikeDetailMovie(likes1.getId(), id);
            return true;
        }

        // 좋아요 목록이 있고, likeDetailMovie에 해당 id의 movie이 있음 -> like 행 업데이트, likeDetailMovie 행 삭제
        deleteLikeDetailMovie(likes1, id);

        // like의 totalCount값이 0이면 like테이블에서 삭제
        if (likes1.getTotalCount() == 0) {
            likeRepository.deleteById(likes1.getId());
        }
        return false;
    }

    @Transactional
    public boolean toggleLikeBook(String userId, String id) {
        List<Likes> likes = getLikeInfo(userId);
        // 좋아요 목록 없음 -> like 행 추가, likeDetailBook 행 추가
        if (likes.isEmpty()) {
            Likes likes1 = createLike(userId);
            createLikeDetailBook(likes1.getId(), id);
            return true;
        }
        // 좋아요 목록은 있지만, likeDetailBook에 해당 id의 book이 없음 -> like 행 업데이트, likeDetailBook 행 추가
        Likes likes1 = likes.getFirst();
        if (!existInLikeDetailBook(likes1.getId(), id)) {
            updateLikeIncrement(likes1);
            createLikeDetailBook(likes1.getId(), id);
            return true;
        }

        // 좋아요 목록이 있고, likeDetailBook에 해당 id의 book이 있음 -> like 행 업데이트, likeDetailBook 행 삭제
        deleteLikeDetailBook(likes1, id);

        // like의 totalCount값이 0이면 like테이블에서 삭제
        if (likes1.getTotalCount() == 0) {
            likeRepository.deleteById(likes1.getId());
        }
        return false;
    }
}