package com.grepp.moodlink.app.model.data.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    private String isbn;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String image;
    private String author;
    private String publisher;
    private LocalDate publishedDate;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String genre;
    private Long likeCount;

}
