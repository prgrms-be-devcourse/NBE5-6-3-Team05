package com.grepp.moodlink.app.model.movie;

import com.grepp.moodlink.app.model.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.movie.entity.Genre;
import com.grepp.moodlink.app.model.movie.entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    public void saveMovies(List<MovieDto> movieDtos) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (MovieDto dto : movieDtos) {
            Movie movie = new Movie();
            movie.setId(String.valueOf(dto.getId()));
            movie.setTitle(dto.getTitle());
            movie.setDescription(dto.getOverview());
            String dateStr = dto.getReleaseDate();
            LocalDate releaseDate = null;
            if (dateStr != null && !dateStr.isBlank()) {
                releaseDate = LocalDate.parse(dateStr, formatter);
            }
            movie.setReleaseDate(releaseDate);
            movie.setThumbnail( "https://image.tmdb.org/t/p/original/" + String.valueOf(dto.getPosterPath()));
            movie.setCreatedAt(LocalDateTime.now());

            List<Integer> genreIds = dto.getGenreIds();
            List<String> genreNames = dto.getGenreNames();
            Map<Integer, String> genreMap = new HashMap<>();
            for(int i = 0 ; i < genreIds.size(); i++){
                genreMap.put(genreIds.get(i), genreNames.get(i));
            }
            Set<Genre> genres = genreMap.entrySet().stream()
                    .map(entry -> genreRepository.findById(Long.valueOf(entry.getKey()))
                            .orElseGet(() -> genreRepository.save(new Genre(entry.getKey(), entry.getValue())))
                    )
                    .collect(Collectors.toSet());
            movie.setGenres(genres);

            movieRepository.save(movie);
        }
    }

}

