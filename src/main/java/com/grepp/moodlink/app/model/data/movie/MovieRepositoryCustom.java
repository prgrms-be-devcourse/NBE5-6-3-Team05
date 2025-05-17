package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepositoryCustom {

    String findTopThumbnail();

    String findTitle();

    Page<Movie> findPaged(Pageable pageable);

    Optional<Movie> findByIdWithGenre(String id);

    void updateBook(MovieInfoDto dto);

    List<MovieDto> searchContent(String contentName);
}
