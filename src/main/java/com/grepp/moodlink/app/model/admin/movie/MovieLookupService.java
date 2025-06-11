package com.grepp.moodlink.app.model.admin.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.moodlink.app.model.admin.movie.dto.TmdbMovieDto;
import com.grepp.moodlink.app.model.admin.movie.repository.AdminGenreRepository;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.admin.movie.dto.TmdbDto;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.response.ResponseCode;
import feign.template.UriUtils;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieLookupService {

    @Value("${lookup.tmdb.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AdminGenreRepository genreRepository;

    public MovieInfoDto lookup(String title) throws JsonProcessingException {

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // URI 빌드 + 인코딩
        URI uri = UriComponentsBuilder
            .fromHttpUrl("https://api.themoviedb.org/3/search/movie")
            .queryParam("query",title)
            .queryParam("language","ko")
            .queryParam("region","KR")
            .encode()                    // <-- 여기서 UTF-8 percent-encoding 수행
            .build()
            .toUri();

        ResponseEntity<String> response = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            entity,
            String.class
        );

        TmdbDto tmdbDto = objectMapper.readValue(response.getBody(), TmdbDto.class);

        TmdbMovieDto dto= tmdbDto.getResults().getFirst();

        if(dto==null){
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        log.info(dto.getOverview());

        return toMovieInfoDto(dto);
    }

    private MovieInfoDto toMovieInfoDto(TmdbMovieDto dto) {

        MovieInfoDto movie = new MovieInfoDto();
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getOverview());
        LocalDate dateStr = dto.getReleaseDate();
        LocalDate releaseDate = null;
        if (dateStr != null) {
            releaseDate = dateStr;
        }
        movie.setReleaseDate(releaseDate);
        movie.setThumbnail(
            "https://image.tmdb.org/t/p/original/" + String.valueOf(dto.getPosterPath()));
        movie.setCreatedAt(LocalDate.now());

        List<Integer> genreIds = dto.getGenreIds();
        Set<Genre> genres = new HashSet<>();

        for(Integer id:genreIds){
            genres.add(genreRepository.findByid(id));
        }

        movie.setGenres(genres);

        return movie;
    }

}
