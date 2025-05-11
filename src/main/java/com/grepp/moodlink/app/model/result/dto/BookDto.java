package com.grepp.moodlink.app.model.result.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDto {
    private String id;
    private String name;
    private String imgUrl;
    private String externalLink;
    private boolean status;

}
