package com.grepp.moodlink.app.controller.api.admin;

import com.grepp.moodlink.app.model.admin.book.AdminBookService;
import com.grepp.moodlink.app.model.admin.movie.AdminMovieService;
import com.grepp.moodlink.app.model.admin.music.AdminMusicService;
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

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
//@PreAuthorize("hasRole('ADMIN')") // postman 호출을 위해 주석 처리함 나중에 풀어야함.
public class AdminApiController {

    private final AdminBookService bookService;
    private final AdminMovieService movieService;
    private final AdminMusicService musicService;
    private final JobLauncher jobLauncher;
    private final Job recommendationJob;

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

}
