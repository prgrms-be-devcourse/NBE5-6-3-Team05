package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.controller.web.member.payload.LikeGenreResponse;
import com.grepp.moodlink.app.model.book.BookRepository;
import com.grepp.moodlink.app.model.book.dto.BookDto;
import com.grepp.moodlink.app.model.book.entity.Book;
import com.grepp.moodlink.app.model.movie.MovieRepository;
import com.grepp.moodlink.app.model.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.movie.entity.Genre;
import com.grepp.moodlink.app.model.movie.entity.Movie;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailBooks;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMovies;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailSong;
import com.grepp.moodlink.app.model.recomend.entity.Likes;
import com.grepp.moodlink.app.model.song.SongRepository;
import com.grepp.moodlink.app.model.song.dto.SongDto;
import com.grepp.moodlink.app.model.song.entity.Song;
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
    private final LikeDetailSongRepository likeDetailSongRepository;
    private final BookRepository bookRepository;
    private final SongRepository songRepository;
    private final MovieRepository movieRepository;

    public List<Likes> getLikeInfo(String userId) {
        return likeRepository.findByUserId(userId);
    }

    public List<LikeDetailBooks> getLikeDetailBook(List<Likes> likes) {
        List<LikeDetailBooks> likeDetailBooks = new ArrayList<>();

        for (Likes like : likes) {
            LikeDetailBooks detailBook = likeDetailBooksRepository.findByLikeId(like.getId());

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
        return books;
    }


    public List<LikeDetailSong> getLikeDetailSong(List<Likes> likes) {
        List<LikeDetailSong> likeDetailSong = new ArrayList<>();

        for (Likes like : likes) {
            LikeDetailSong detailSong = likeDetailSongRepository.findByLikeId(like.getId());

            if (detailSong != null) {
                likeDetailSong.add(detailSong);
            }
        }

        return likeDetailSong;

    }

    public List<SongDto> getSongList(List<LikeDetailSong> likeDetailSongs) {
        List<SongDto> songs = new ArrayList<>();

        for (LikeDetailSong likeDetailsong : likeDetailSongs) {
            Song song = songRepository.findById(likeDetailsong.getSongId()).orElse(null);

            if (song != null) {
                songs.add(SongDto.toDto(song));
            }
        }
        return songs;
    }

    public List<LikeDetailMovies> getLikeDetailMovie(List<Likes> likes) {
        List<LikeDetailMovies> likeDetailMovies = new ArrayList<>();

        for (Likes like : likes) {
            LikeDetailMovies detailMovies = likeDetailMoviesRepository.findByLikeId(like.getId());

            if (detailMovies != null) {
                likeDetailMovies.add(detailMovies);
            }
        }

        return likeDetailMovies;

    }

    public List<MovieDto> getMovieList(List<LikeDetailMovies> likeDetailMovies) {
        List<MovieDto> movies = new ArrayList<>();

        for (LikeDetailMovies likeDetailMovie : likeDetailMovies) {
            Movie movie = movieRepository.findById(likeDetailMovie.getMovieId()).orElse(null);

            if (movie != null) {
                movies.add(MovieDto.toDto(movie));
            }
        }
        return movies;
    }


    @Transactional
    public List<SongDto> getUserLikedSong(String userId) {
        List<Likes> likes = getLikeInfo(userId);
        List<LikeDetailSong> likeDetailSong = getLikeDetailSong(likes);
        return getSongList(likeDetailSong);
    }

    @Transactional
    public List<BookDto> getUserLikedBooks(String userId) {
        List<Likes> likes = getLikeInfo(userId);
        List<LikeDetailBooks> likeDetailBooks = getLikeDetailBook(likes);
        return getBookList(likeDetailBooks);
    }

    @Transactional
    public List<MovieDto> getUserLikedMovies(String userId) {
        List<Likes> likes = getLikeInfo(userId);
        List<LikeDetailMovies> likeDetailMovies = getLikeDetailMovie(likes);
        return getMovieList(likeDetailMovies);
    }
    @Transactional
    public List<LikeGenreResponse> getPersonalLikeMovieGenre(String userId) {
        List<MovieDto> movies = getUserLikedMovies(userId);

        Map<String, Long> genreCount = new HashMap<>();

        for (MovieDto movie : movies) {
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
                if (genreName != null && !genreName.isBlank()) {
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
    public List<LikeGenreResponse> getPersonalLikeSongGenre(String userId) {
        List<SongDto> songs = getUserLikedSong(userId);

        Map<String, Long> genreCount = new HashMap<>();

        for (SongDto song : songs) {
            String genre = song.getGenre();
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
    public List<LikeGenreResponse> getMostLikeSongGenre() {

        List<Song> songs = songRepository.findAll();
        Map<String, Long> genreCount = new HashMap<>();

        for (Song song : songs) {
            String genre = song.getGenre();
            Long likeCount = song.getLikeCount();
            if (genre != null && !genre.isBlank()) {
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
            if (genre != null && !genre.isBlank()) {
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



