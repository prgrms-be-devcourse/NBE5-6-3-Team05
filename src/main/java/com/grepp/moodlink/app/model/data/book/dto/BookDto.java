package com.grepp.moodlink.app.model.data.book.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookDto {

    @JsonProperty("ISBN")
    private String isbn;
    private String title;
    private String image;
    private String author;
    private String publisher;
    @JsonProperty("pubdate")
    private LocalDate publishedDate;
    private String description;
    private String genre;

    public static BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setImage(book.getImage());
        dto.setAuthor(book.getAuthor());
        dto.setPublisher(book.getPublisher());
        dto.setPublishedDate(book.getPublishedDate());
        dto.setDescription(book.getDescription());
        dto.setGenre(book.getGenre());

        return dto;
    }
}
