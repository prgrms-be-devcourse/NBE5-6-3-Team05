package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepositoryCustom {

    String findTopThumbnail();

    String findTitle();

    List<MovieDto> searchContent(String contentName);

    Optional<Movie> findByIdWithGenre(String id);
}
