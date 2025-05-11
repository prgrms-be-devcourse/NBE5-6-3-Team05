package com.grepp.moodlink.infra.app.model.result.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieDto {
    private String name;
    private String imgUrl;
    private String externalLink;
    private boolean status;

}
