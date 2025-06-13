package com.grepp.moodlink.app.controller.api.details.payload;

import com.grepp.moodlink.app.model.data.movie.dto.GenreDto;
import com.grepp.moodlink.app.model.details.dto.MovieDetailsDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailResponse {
   private MovieDetailsDto movie;
   private List<GenreDto> movieGenre;

}
