package com.grepp.moodlink.app.model.details.dto;

import com.grepp.moodlink.app.model.data.book.entity.Book;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDetailsDto {
    private String id;
    private String name;
    private String author;
    private LocalDate publishedAt;
    private String description;
    private String bookImg;
    private String externalLink;
    private Boolean status;
    private String genre;
    private String publisher;
    private String summary;

    public BookDetailsDto(String id, String name, String author, LocalDate publishedAt, String description,
        String bookImg, String genre, String publisher, String summary) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publishedAt = publishedAt;
        this.description = description;
        this.bookImg = bookImg;
        this.genre = genre;
        this.publisher = publisher;
        this.summary = summary;
        this.status = false;
        this.externalLink = "https://www.google.com/search?q="+name;
    }

    public static BookDetailsDto from(Book book){
        return new BookDetailsDto(
            book.getIsbn(),
            book.getTitle(),
            book.getAuthor(),
            book.getPublishedDate(),
            book.getDescription(),
            book.getImage(),
            book.getGenre(),
            book.getPublisher(),
            book.getSummary()
        );
    }
}
