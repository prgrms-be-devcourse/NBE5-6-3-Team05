package com.grepp.moodlink.app.model.result.dto;

import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieSimpleDto {
    private String id;
    private String name;
    private String imgUrl;
    private String externalLink;
    private boolean status;

    public MovieSimpleDto(String id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.status =false;
        this.externalLink = "https://www.google.com/search?q="+name;
    }

    public static MovieSimpleDto from(MovieInfoDto movie){
        return new MovieSimpleDto(movie.getId(), movie.getTitle(), movie.getThumbnail());
    }

}
