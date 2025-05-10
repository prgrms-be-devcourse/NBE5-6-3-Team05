package com.grepp.moodlink.app.model.data.book.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookDto {
    @JsonProperty("ISBN")
    private Integer isbn;
    private String title;
    private String image;
    private String author;
    private String publisher;
    @JsonProperty("pubdate")
    private LocalDate publishedDate;
    private String description;
    private String genre;
}
