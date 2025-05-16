package com.grepp.moodlink.app.model.data.book.entity;

import com.grepp.moodlink.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {

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
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;    @Column(columnDefinition = "TEXT")
    private String summary;
    @Column(columnDefinition = "BLOB")
    private byte[] embedding;
    private String genre;
    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long likeCount;
    // 정렬을 위해 일단 임시로...
    @LastModifiedDate
    protected LocalDate modifiedAt=LocalDate.now();
}
