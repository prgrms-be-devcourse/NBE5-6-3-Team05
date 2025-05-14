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

    public BookDto(String id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }
}
