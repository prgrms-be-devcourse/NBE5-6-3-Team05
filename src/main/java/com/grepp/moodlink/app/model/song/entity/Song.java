package com.grepp.moodlink.app.model.song.entity;

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
@Table(name = "song")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song
{
    @Id
    private String id;
    @Column(nullable = false)
    private String title;
    private String genre;
    private String singer;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate releaseDate;
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;
    private Long likeCount;
}
