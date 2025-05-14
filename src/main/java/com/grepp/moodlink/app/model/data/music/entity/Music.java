package com.grepp.moodlink.app.model.data.music.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "music")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Music {

    @Id
    private String id;
    private String title;
    private String genre;
    private String singer;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String lyrics;

    @Column(columnDefinition = "BLOB")
    private byte[] embedding;
    private LocalDate releaseDate;
    private LocalDate createdAt;
    @Column(columnDefinition = "TEXT")
    private String thumbnail;
    private Long likeCount;

    private Boolean activated = true;
    public void unActivated() {
        this.activated = false;
    }
}
