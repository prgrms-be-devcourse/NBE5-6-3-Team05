package com.grepp.moodlink.app.model.book.dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookDto {
    private Integer isbn;
    private String title;
    private String image;
    private String author;
    private String publisher;
    private LocalDate publishedDate;
    private String description;
    private String genre;
}
