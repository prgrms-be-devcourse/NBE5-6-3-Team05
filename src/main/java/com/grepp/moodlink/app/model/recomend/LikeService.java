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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public List<Likes> getLikeInfo(String userId) {
        return likeRepository.findByUserId(userId);
    }

    public List<LikeDetailBooks> getLikeDetailBook(List<Likes> likes) {
        List<LikeDetailBooks> likeDetailBooks = new ArrayList<>();

        for (Likes like : likes) {
            LikeDetailBooks detailBook = likeDetailBooksRepository.findByLikesId(like.getId());

            if (detailBook != null) {
                likeDetailBooks.add(detailBook);
            }
        }

        return likeDetailBooks;

    }

    public List<BookDto> getBookList(List<LikeDetailBooks> likeDetailBooks) {
        List<BookDto> books = new ArrayList<>();

        for (LikeDetailBooks likeDetailBook : likeDetailBooks) {
            Book book = bookRepository.findById(likeDetailBook.getBookId()).orElse(null);

            if (book != null) {
                books.add(BookDto.toDto(book));
            }
        }

        log.info("Books: {}", books);

        return books;
    }


    public List<LikeDetailMusic> getLikeDetailMusic(List<Likes> likes) {
        List<LikeDetailMusic> likeDetailMusics = new ArrayList<>();

        for (Likes like : likes) {
            LikeDetailMusic detailMusic = likeDetailMusicRepository.findByLikesId(like.getId());

            if (detailMusic != null) {
                likeDetailMusics.add(detailMusic);
            }
        }

        return likeDetailMusics;

    }

    public List<MusicDto> getMusicList(List<LikeDetailMusic> likeDetailMusics) {
        List<MusicDto> musics = new ArrayList<>();

        for (LikeDetailMusic likeDetailMusic : likeDetailMusics) {
            Music music = musicRepository.findById(likeDetailMusic.getMusicId()).orElse(null);

            if (music != null) {
                musics.add(MusicDto.toDto(music));
            }
        }
        return musics;
    }

    public List<LikeDetailMovies> getLikeDetailMovie(List<Likes> likes) {
        List<LikeDetailMovies> likeDetailMovies = new ArrayList<>();

        for (Likes like : likes) {
            LikeDetailMovies detailMovies = likeDetailMoviesRepository.findByLikesId(like.getId());

            if (detailMovies != null) {
                likeDetailMovies.add(detailMovies);
            }
        }

        return likeDetailMovies;

    }

    public List<MovieInfoDto> getMovieList(List<LikeDetailMovies> likeDetailMovies) {
        List<MovieInfoDto> movies = new ArrayList<>();

        for (LikeDetailMovies likeDetailMovie : likeDetailMovies) {
            Movie movie = movieRepository.findById(likeDetailMovie.getMovieId()).orElse(null);

            if (movie != null) {
                movies.add(MovieInfoDto.toDto(movie));
            }
        }
        return movies;
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
        log.info("bookDto:{}", books);

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
}



