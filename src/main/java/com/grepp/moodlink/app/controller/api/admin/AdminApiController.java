package com.grepp.moodlink.app.controller.api.admin;

import com.grepp.moodlink.app.controller.api.admin.payload.GenreAddRequest;
import com.grepp.moodlink.app.controller.api.admin.payload.GenreModifyRequest;
import com.grepp.moodlink.app.model.admin.book.AdminBookService;
import com.grepp.moodlink.app.model.admin.movie.AdminMovieService;
import com.grepp.moodlink.app.model.admin.music.AdminMusicService;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.infra.response.ApiResponse;
import com.grepp.moodlink.infra.response.ResponseCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminApiController {

    private final AdminBookService bookService;
    private final AdminMovieService movieService;
    private final AdminMusicService musicService;
    private final JobLauncher jobLauncher;
    private final Job recommendationJob;
    private final AdminMovieService adminMovieService;
    private final AdminMusicService adminMusicService;
    private final AdminBookService adminBookService;

    // 영화 삭제
    @DeleteMapping("movies/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }

    // 영화 삭제
    @DeleteMapping("music/delete/{id}")
    public ResponseEntity<String> deleteMusic(@PathVariable String id) {
        musicService.deleteMusic(id);
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }


    // 도서 삭제
    @DeleteMapping("books/delete/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }

    // 추천 결과 생성
    @PostMapping("/recommend")
    public ResponseEntity<String> generateRecommend(){
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(recommendationJob, jobParameters);
            return ResponseEntity.ok("배치 잡이 실행되었습니다.");
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            return ResponseEntity.internalServerError().body("배치 실행 실패: " + e.getMessage());
        }
    }

    // 장르 추가
    @PostMapping("genres/add")
    public ResponseEntity<ApiResponse<String>> addGenre(@RequestBody GenreAddRequest genreAddRequest) {

        switch (genreAddRequest.getContentType()) {
            case "MOVIE" -> adminMovieService.addGenre(genreAddRequest.toGenreDto());
            case "MUSIC" -> adminMusicService.addGenre(genreAddRequest.toMusicGenreDto());
            case "BOOK" -> adminBookService.addGenre(genreAddRequest.toBookGenreDto());
            case null, default -> {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ResponseCode.BAD_REQUEST));
            }
        }

        return ResponseEntity.ok(ApiResponse.success("정상적으로 입력되었습니다."));
    }

    // 장르 수정
    @PutMapping("genres/modify/{id}")
    public ResponseEntity<ApiResponse<String>> modifyGenre(@PathVariable("id") String id, @RequestBody GenreModifyRequest genreModifyRequest) {

        Long longId = Long.parseLong(id);

        switch (genreModifyRequest.getContentType()) {
            case "MOVIE" -> adminMovieService.modifyGenre(longId.intValue(), genreModifyRequest.toGenreDto());
            case "MUSIC" -> adminMusicService.modifyGenre(longId, genreModifyRequest.toMusicGenreDto());
            case "BOOK" -> adminBookService.modifyGenre(longId, genreModifyRequest.toBookGenreDto());
            case null, default -> {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ResponseCode.BAD_REQUEST));
            }
        }

        return ResponseEntity.ok(ApiResponse.success("정상적으로 입력되었습니다."));



    }


    // 장르 삭제
    @DeleteMapping("genres/delete/{content}/{id}")
    public ResponseEntity<ApiResponse<String>> deleteGenre(@PathVariable("content") String content ,@PathVariable("id") String id) {

        Long longId = Long.parseLong(id);
        Boolean isDeleted;
        switch (content) {
        case "MOVIE" -> isDeleted = adminMovieService.deleteGenre(longId.intValue());
        case "MUSIC" -> isDeleted = adminMusicService.deleteGenre(longId);
        case "BOOK" -> isDeleted = adminBookService.deleteGenre(longId);
        case null, default -> {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(ResponseCode.BAD_REQUEST));
            }
        }

        if(isDeleted)
            return ResponseEntity.ok(ApiResponse.success("정상적으로 입력되었습니다."));
        return ResponseEntity.badRequest().body(ApiResponse.error(ResponseCode.BAD_REQUEST));
    }
}
