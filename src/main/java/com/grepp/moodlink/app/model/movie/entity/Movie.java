package com.grepp.moodlink.app.model.movie.entity;

import com.grepp.moodlink.app.model.embedding.FloatArrayConverter;
import com.grepp.moodlink.infra.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "movie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends BaseEntity {
    @Id
    private String id;
    @Column(nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "BLOB")
    private byte[] embedding;
    private LocalDate releaseDate;
    private LocalDateTime createdAt;
    @Column(columnDefinition = "TEXT")
    private String thumbnail;
    private Long likeCount;
}

