package com.grepp.moodlink.app.controller.api.movie;


import com.grepp.moodlink.app.model.data.movie.MovieService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/movie")
public class MovieApiController {

    private final MovieService movieService;

    // worldcup 생성 시 영화 목록 전달(추후 LLM으로 변경 예정)
    @GetMapping("/worldcup")
    public ResponseEntity<List<Map<String,Object>>> getMovies(){
        return ResponseEntity.ok(movieService.getMovieList());
    }
}
