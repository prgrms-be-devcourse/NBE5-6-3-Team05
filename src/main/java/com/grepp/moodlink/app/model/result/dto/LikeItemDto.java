package com.grepp.moodlink.app.model.result.dto;

import java.util.List;
import lombok.Data;

@Data
public class LikeItemDto {
    private List<BookDto> books;
    private List<SongDto> songs;
    private List<MovieDto> movies;

}
