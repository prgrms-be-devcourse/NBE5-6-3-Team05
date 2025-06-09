package com.grepp.moodlink.app.controller.api.admin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.grepp.moodlink.app.model.admin.movie.MovieLookupService;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.infra.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Api를 통해 정보를 가져오는 Controller
@RestController
@RequestMapping("api/admin/lookup")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminApiLookuplController {

    private final MovieLookupService movieLookupService;

    @GetMapping("movie/{title}")
    public ResponseEntity<ApiResponse<MovieInfoDto>> lookUpMovie(@PathVariable String title) {
        try{
            MovieInfoDto movie = movieLookupService.lookup(title);
            log.info(movie.getGenres().toString());
            return ResponseEntity.ok(ApiResponse.success(movie));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(null);

        }
    }

    @GetMapping("book")
    public ResponseEntity<ApiResponse<MovieInfoDto>> lookUpBook(@RequestParam String title)
        throws JsonProcessingException {
        //TODO 내용 바꾸기
        MovieInfoDto movie = movieLookupService.lookup(title);

        return ResponseEntity.ok(ApiResponse.success(movie));

    }

    @GetMapping("music")
    public ResponseEntity<ApiResponse<MovieInfoDto>> lookUpMusic(@RequestParam String title)
        throws JsonProcessingException {
        //TODO 내용 바꾸기
        MovieInfoDto movie = movieLookupService.lookup(title);

        return ResponseEntity.ok(ApiResponse.success(movie));

    }
}
