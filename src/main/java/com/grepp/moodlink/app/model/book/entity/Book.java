package com.grepp.moodlink.app.model.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String id;

    @Column(nullable = false)
    private String title;
    private String genre;
    private String author;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate publishDate;
    private LocalDateTime createdAt;
    @Column(columnDefinition = "TEXT")
    private String thumbnail;
    private Long likeCount;
}