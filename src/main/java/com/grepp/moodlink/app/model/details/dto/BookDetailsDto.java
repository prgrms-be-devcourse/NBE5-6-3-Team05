package com.grepp.moodlink.app.model.details.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDetailsDto {
    private String name;
    private String author;
    private LocalDateTime publishedAt;
    private String description;
    private String bookImg;
}
