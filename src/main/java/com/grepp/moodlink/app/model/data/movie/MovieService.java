package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.dto.GenreDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.imgbb.ImgUploadTemplate;
import com.grepp.moodlink.infra.response.ResponseCode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ModelMapper mapper;
    private final ImgUploadTemplate imgUploadTemplate;


    public void saveMovies(List<MovieDto> movieDtos) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (MovieDto dto : movieDtos) {
            Movie movie = new Movie();
            long count = movieRepository.count();
            movie.setId("M" + count);
            movie.setTitle(dto.getTitle());
            movie.setDescription(dto.getOverview());
            LocalDate dateStr = dto.getReleaseDate();
            LocalDate releaseDate = null;
            if (dateStr != null ) {
                releaseDate = dateStr;
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

    public Page<MovieInfoDto> findPaged(Pageable pageable){
        return movieRepository.findPaged(pageable)
            .map(MovieInfoDto::toDto);
    }

    // 모든 장르를 가져옴
    public List<GenreDto> findAllGenre() {
        return genreRepository.findAll().stream().map(e-> mapper.map(e, GenreDto.class)).toList();
    }

    // 영화를 추가함
    public void addMovie(List<MultipartFile> thumbnail, MovieInfoDto dto) {

        if(movieRepository.existsByTitleAndReleaseDate(dto.getTitle(), dto.getReleaseDate()))
            throw new CommonException(ResponseCode.DUPLICATED_DATA);

        try {

            uploadImage(thumbnail, dto);
            Movie movie = mapper.map(dto, Movie.class);

            long count = movieRepository.count();
            movie.setId("M"+count);

            log.info("{}",movie);

            // 자동으로 안 들어가네
            movie.setCreatedAt(LocalDate.now());

            movieRepository.save(movie);
        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void uploadImage(List<MultipartFile> thumbnail, MovieInfoDto dto) throws IOException {
        if(thumbnail != null){
            MultipartFile file =  thumbnail.getFirst();
            String originFileName = file.getOriginalFilename();
            if (originFileName != null && originFileName.contains(".")) {
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String renameFileName = UUID.randomUUID().toString() + ext;

                String thumbnailUrl = imgUploadTemplate.uploadImage(file, renameFileName);
                dto.setThumbnail(thumbnailUrl);
            }
        }
    }

    public MovieInfoDto findById(String id) {
        return movieRepository.findByIdWithGenre(id).map(MovieInfoDto::toDto).orElse(null);
    }

    public void updateMovie(List<MultipartFile> thumbnail, MovieInfoDto dto) {

        try {
            uploadImage(thumbnail, dto);

            // 업데이트
            movieRepository.updateBook(dto);

            log.info("{}",dto);


        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Transactional
    public void deleteMovie(String id) {
        movieRepository.findById(id).ifPresent(Movie::unActivated);
    }

    @Transactional
    public List<String> parseRecommend(String movieResult) {
        List<String> result = new ArrayList<>();
        if (movieResult == null || movieResult.isBlank()) return result;

        String line = movieResult.trim().replaceFirst("^[가-힣a-zA-Z0-9\\s:]+", "");

        Matcher m = Pattern.compile("\"([^\"]+)\"").matcher(line);
        while (m.find()) {
            String title = m.group(1).trim();
            if (title.startsWith("[") && title.endsWith("]")) {
                title = title.substring(1, title.length()-1).trim();
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
}
