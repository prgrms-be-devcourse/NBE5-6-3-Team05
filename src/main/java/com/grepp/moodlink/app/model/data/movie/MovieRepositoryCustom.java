package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import java.nio.channels.FileChannel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepositoryCustom {

    String findTopThumbnail();

    String findTitle();

    String findDescription();

    Page<Movie> findPaged(Pageable pageable);

    Optional<Movie> findByIdWithGenre(String id);

    void updateBook(MovieInfoDto dto);
}
