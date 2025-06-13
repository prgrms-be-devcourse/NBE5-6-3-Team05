package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.dto.GenreDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.music.dto.MusicGenreDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.data.music.entity.MusicGenre;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;


    public void saveMovies(List<MovieDto> movieDtos) {

        for (MovieDto dto : movieDtos) {
            Movie movie = new Movie();
            long count = movieRepository.count();
            movie.setId("M" + count);
            movie.setTitle(dto.getTitle());
            movie.setDescription(dto.getOverview());
            LocalDate dateStr = dto.getReleaseDate();
            LocalDate releaseDate = null;
            if (dateStr != null) {
                releaseDate = dateStr;
            }
            movie.setReleaseDate(releaseDate);
            movie.setThumbnail(
                "https://image.tmdb.org/t/p/original/" + dto.getPosterPath());
            movie.setCreatedAt(LocalDate.now());

            List<Integer> genreIds = dto.getGenreIds();
            List<String> genreNames = dto.getGenreNames();
            Map<Integer, String> genreMap = new HashMap<>();
            for (int i = 0; i < genreIds.size(); i++) {
                genreMap.put(genreIds.get(i), genreNames.get(i));
            }
            Set<Genre> genres = genreMap.entrySet().stream()
                .map(entry -> genreRepository.findById(entry.getKey())
                    .orElseGet(
                        () -> genreRepository.save(new Genre(entry.getKey(), entry.getValue())))
                )
                .collect(Collectors.toSet());
            movie.setGenres(genres);

            movieRepository.save(movie);
        }
    }

    public List<String> parseRecommend(String movieResult) {
        List<String> result = new ArrayList<>();
        if (movieResult == null || movieResult.isBlank()) {
            return result;
        }

        String line = movieResult.trim().replaceFirst("^[가-힣a-zA-Z0-9\\s:]+", "");

        Matcher m = Pattern.compile("\"([^\"]+)\"").matcher(line);
        while (m.find()) {
            String title = m.group(1).trim();
            if (title.startsWith("[") && title.endsWith("]")) {
                title = title.substring(1, title.length() - 1).trim();
            }
            result.add(title);
        }
        return result.stream()
            .map(movieRepository::findIdByTitle)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .distinct()
            .collect(Collectors.toList());
    }

    public MovieInfoDto findById(String id) {
        return movieRepository.findByIdWithGenre(id).map(MovieInfoDto::toDto).orElse(null);
    }

    @Transactional
    public void incrementLikeCount(String id) {
        Movie movie = movieRepository.findById(id).orElseThrow();
        Long currentCount = movie.getLikeCount();
        movie.setLikeCount(currentCount + 1);
    }

    @Transactional
    public void decreaseLikeCount(String id) {
        Movie movie = movieRepository.findById(id).orElseThrow();
        Long currentCount = movie.getLikeCount();
        movie.setLikeCount(currentCount - 1);
    }


    // dto에서의 id의 type을 변경해야 함.
    public List<Map<String, Object>> getMovieList() {
        // Dto 대신 임의의 Map<>을 통해 전달("id" : 영화 컨텐츠의 id, "title": 영화 컨텐츠의 제목)
        return movieRepository.findAll().stream()
            .map(m -> {

                // MovieDto의 id는 int형이기에 String인 entity의 id를 받아서 전달
                //{
                //  "id":movie의 id,
                //  "title":movie의 title
                // }
                Map<String, Object> map = new HashMap<>();
                map.put("id", m.getId());
                map.put("title", m.getTitle());

                // 함수형 내부에서의 return(메서드의 반환이 아님.)
                return map;
            })
            .collect(Collectors.toList());

    }

    public List<GenreDto> getMovieGenre(String id) {
        Movie movie = movieRepository.findById(id).orElseThrow();
        Set<Genre> genres = movie.getGenres();
        return genres.stream()
            .map(genre -> new GenreDto(genre.getId(), genre.getName()))
            .collect(Collectors.toList());
    }
}
