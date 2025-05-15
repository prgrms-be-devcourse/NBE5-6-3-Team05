package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            long count = movieRepository.count();
            movie.setId("M" + count);
            movie.setTitle(dto.getTitle());
            movie.setDescription(dto.getOverview());
            String dateStr = dto.getReleaseDate();
            LocalDate releaseDate = null;
            if (dateStr != null && !dateStr.isBlank()) {
                releaseDate = LocalDate.parse(dateStr, formatter);
            }
            movie.setReleaseDate(releaseDate);
            movie.setThumbnail(
                "https://image.tmdb.org/t/p/original/" + String.valueOf(dto.getPosterPath()));
            movie.setCreatedAt(LocalDate.now());

            List<Integer> genreIds = dto.getGenreIds();
            List<String> genreNames = dto.getGenreNames();
            Map<Integer, String> genreMap = new HashMap<>();
            for(int i = 0 ; i < genreIds.size(); i++){
                genreMap.put(genreIds.get(i), genreNames.get(i));
            }
            Set<Genre> genres = genreMap.entrySet().stream()
                    .map(entry -> genreRepository.findById(entry.getKey())
                            .orElseGet(() -> genreRepository.save(new Genre(entry.getKey(), entry.getValue())))
                    )
                    .collect(Collectors.toSet());
            movie.setGenres(genres);

            movieRepository.save(movie);
        }
    }

    @Transactional
    public List<Movie> parseRecommend(String movieResult) {
        List<String> titles = new ArrayList<>();
        Pattern pattern = Pattern.compile("^\\d+\\.\\s*(.*?)\\s*(?=:)", Pattern.MULTILINE);
        String[] lines = movieResult.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String title = processEnclosures(matcher.group(1).trim());
                titles.add(title);
            }
        }
        return titles.stream()
                .map(movieRepository::findByTitle)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());
    }

    public static String processEnclosures(String title) {
        String[][] enclosures = {{"\"", "\""}, {"[", "]"}};
        for (String[] enc : enclosures) {
            String start = enc[0];
            String end = enc[1];
            if (title.startsWith(start) && title.endsWith(end)) {
                title = title.substring(start.length(), title.length() - end.length()).trim();
            }
        }
        return title.replace("\"", "");
    }
}
