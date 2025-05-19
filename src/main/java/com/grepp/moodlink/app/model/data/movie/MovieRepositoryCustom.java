package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepositoryCustom {

    String findTopThumbnail();

    String findTitle();

    List<MovieDto> searchContent(String contentName);
}
