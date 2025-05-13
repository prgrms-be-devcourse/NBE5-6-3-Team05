package com.grepp.moodlink.app.model.book.dto;

import com.grepp.moodlink.app.model.book.entity.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BookDto {


    private String id;
    private String title;
    private String genre;
    private String author;
    private String description;
    private LocalDate publishDate;
    private LocalDateTime createdAt;
    private String thumbnail;
    private Long likeCount;

    public static BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setGenre(book.getGenre());
        dto.setAuthor(book.getAuthor());
        dto.setDescription(book.getDescription());
        dto.setPublishDate(book.getPublishDate());
        dto.setCreatedAt(book.getCreatedAt());
        dto.setThumbnail(book.getThumbnail());
        dto.setLikeCount(book.getLikeCount());

        return dto;
    }
}
