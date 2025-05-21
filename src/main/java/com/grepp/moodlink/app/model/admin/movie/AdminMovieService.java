package com.grepp.moodlink.app.model.admin.movie;

import com.grepp.moodlink.app.model.data.movie.dto.GenreDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.llm.EmbeddingService;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.imgbb.ImgUploadTemplate;
import com.grepp.moodlink.infra.response.ResponseCode;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
public class AdminMovieService {

    private final AdminMovieRepository movieRepository;
    private final AdminGenreRepository genreRepository;
    private final EmbeddingService embeddingService;
    private final ModelMapper mapper;
    private final ImgUploadTemplate imgUploadTemplate;

    // 페이지네이션으로 모든 영화를 가져옴
    public Page<MovieInfoDto> findPaged(Pageable pageable) {
        return movieRepository.findPaged(pageable)
            .map(MovieInfoDto::toDto);
    }

    // 모든 장르를 가져옴
    public List<GenreDto> findAllGenre() {
        return genreRepository.findAll().stream().map(e -> mapper.map(e, GenreDto.class)).toList();
    }

    // 영화를 추가함
    public void addMovie(List<MultipartFile> thumbnail, MovieInfoDto dto) {

        if (movieRepository.existsByTitleAndReleaseDate(dto.getTitle(), dto.getReleaseDate())) {
            throw new CommonException(ResponseCode.DUPLICATED_DATA);
        }

        try {

            uploadImage(thumbnail, dto);
            Movie movie = mapper.map(dto, Movie.class);

            long count = movieRepository.count();
            movie.setId("M" + count);

            log.info("{}", movie);

            movie.setCreatedAt(LocalDate.now());

            // 입력한 데이터 저장
            movieRepository.save(movie);
            // 입력한 데이터를 바탕으로 임베딩값 생성
            embeddingService.generateEmbeddingsMovie();
        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    // 추가 및 수정 시 이미지 업로드
    private void uploadImage(List<MultipartFile> thumbnail, MovieInfoDto dto) throws IOException {
        if (thumbnail != null) {
            MultipartFile file = thumbnail.getFirst();
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
        return movieRepository.findByIdWithGenre(id)
            .map(MovieInfoDto::toDto)
            .orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));
    }

    // 관리자가 직접 영화 정보 수정
    public void updateMovie(List<MultipartFile> thumbnail, MovieInfoDto dto) {

        try {
            uploadImage(thumbnail, dto);

            // 업데이트
            movieRepository.updateBook(dto);
            embeddingService.generateEmbeddingsMovie();
            log.info("{}", dto);


        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    // 영화 삭제 (소프트 드랍)
    @Transactional
    public void deleteMovie(String id) {
        movieRepository.findById(id).ifPresent(Movie::unActivated);
    }

}
