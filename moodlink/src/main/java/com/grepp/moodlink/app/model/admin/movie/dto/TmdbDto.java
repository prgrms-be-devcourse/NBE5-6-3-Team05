package com.grepp.moodlink.app.model.admin.movie.dto;

import java.util.List;
import lombok.Data;

@Data
public class TmdbDto {

    Integer page;
    List<TmdbMovieDto> results;
    Integer total_pages;
    Integer total_results;
}
