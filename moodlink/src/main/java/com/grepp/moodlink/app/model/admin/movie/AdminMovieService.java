package com.grepp.moodlink.app.model.admin.movie;

import com.grepp.moodlink.app.model.admin.movie.repository.AdminGenreRepository;
import com.grepp.moodlink.app.model.admin.movie.repository.AdminMovieRepository;
import com.grepp.moodlink.app.model.data.movie.dto.GenreDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.llm.EmbeddingService;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.imgbb.ImgUploadTemplate;
import com.grepp.moodlink.infra.response.ResponseCode;
import jakarta.persistence.OptimisticLockException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
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
        return genreRepository.findAllByActivated(true).stream().map(e -> mapper.map(e, GenreDto.class)).toList();
    }

    // 영화를 추가함
    @Transactional
    public void addMovie(List<MultipartFile> thumbnail, MovieInfoDto dto) {

        if (movieRepository.existsByTitleAndReleaseDate(dto.getTitle(), dto.getReleaseDate())) {
            throw new CommonException(ResponseCode.DUPLICATED_DATA);
        }

        try {
            // api를 통해 가져왔을 때 thumbnail의 path 값을 가져오기에 따로 처리1

            String ThumbnailImg = dto.getThumbnail();
            if(thumbnail!=null&&!thumbnail.getFirst().isEmpty()){
                uploadImage(thumbnail, dto);
                ThumbnailImg = dto.getThumbnail();
            }
            Movie movie = mapper.map(dto, Movie.class);
            movie.setThumbnail(ThumbnailImg);
            long count = movieRepository.count();
            movie.setId("M" + count);

            movie.setCreatedAt(LocalDate.now());

            // 입력한 데이터 저장
            movieRepository.save(movie);
            // 만약 동시에 접근하여 같은 Id를 가지게 될 경우 방지
            movieRepository.flush();
            // 입력한 데이터를 바탕으로 임베딩값 생성
            embeddingService.generateEmbeddingsMovie();
        } catch (IOException | DataIntegrityViolationException e) {
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
    @Transactional
    public void updateMovie(List<MultipartFile> thumbnail, MovieInfoDto dto) {

        try {
            Movie data = movieRepository.findById(dto.getId()).orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));
            if(thumbnail!=null&&!thumbnail.getFirst().isEmpty()){
                uploadImage(thumbnail, dto);
                String ThumbnailImg = dto.getThumbnail();
                data.setThumbnail(ThumbnailImg);
            }

            data.setGenres(dto.getGenres());
            data.setDescription(dto.getDescription());

            // 업데이트
            movieRepository.save(data);
            embeddingService.generateEmbeddingsMovie();

        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    // 영화 삭제 (소프트 드랍)
    @Transactional
    public void deleteMovie(String id) {
        movieRepository.findById(id).ifPresent(Movie::unActivated);
    }

    // 해당 장르를 사용하는 영화 수 카운트
    public Long countMoviesByGenre(Integer id) {
        return movieRepository.countMoviesByGenre(id);
    }

    // 장르 추가
    @Transactional
    public void addGenre(GenreDto genreDto) {
        if(genreRepository.findByName((genreDto.getName()))!=null){
            throw new CommonException(ResponseCode.DUPLICATED_DATA);
        }

        // id 임의로 20001 부터
        Genre genre = new Genre((int) (movieRepository.count()+20000L), genreDto.getName(),true);

        try{
            genreRepository.save(genre);
            // 동시에 접근 했을 때 같은 id로 insert 하는 것 방지
            // flush 단계에서 예외 잡기
            genreRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    // 장르 삭제
    @Transactional
    public Boolean deleteGenre(int id) {
        // 해당 장르인 영화가 하나라도 있다면 삭제 불가능
        if(countMoviesByGenre(id)!=0)
            return false;
        genreRepository.findById(id).ifPresent(Genre::unActivated);
        return true;
    }

    // 장르 수정
    @Transactional
    public void modifyGenre(Integer longId, GenreDto genreDto) {
        Genre genre = genreRepository.findById(longId).get();
        genre.setName(genreDto.getName());
        genreRepository.save(genre);
    }
}
