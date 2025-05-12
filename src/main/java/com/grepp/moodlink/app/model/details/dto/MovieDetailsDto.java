package com.grepp.moodlink.app.model.details.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieDetailsDto {
    private String id;
    private String name;
    private String director;
    private String genre;
    private String description;
    private String movieUrl;
    private String externalLink;
    private boolean status;
}
