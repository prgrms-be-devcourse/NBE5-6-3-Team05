package com.grepp.moodlink.app.model.data.movie.entity;

import com.grepp.moodlink.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

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
    @Column(columnDefinition = "TEXT")
    private String summary;

    private LocalDate releaseDate;
    private LocalDate createdAt;
    @Column(columnDefinition = "TEXT")
    private String thumbnail;
    @Column(nullable = false,columnDefinition = "BIGINT DEFAULT 0" )
    private Long likeCount;

    private Boolean activated = true;

    public void unActivated() {
        this.activated = false;
    }

    // 정렬을 위해 일단 임시로...
    @LastModifiedDate
    protected LocalDate modifiedAt = LocalDate.now();

}