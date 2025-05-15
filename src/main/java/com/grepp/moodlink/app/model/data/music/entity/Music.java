package com.grepp.moodlink.app.model.data.music.entity;

import com.grepp.moodlink.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "music")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Music extends BaseEntity {

    @Id
    private String id;
    private String title;
    private String genre;
    private String singer;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String lyrics;
    @Column(columnDefinition = "TEXT")
    private String summary;
    @Column(columnDefinition = "BLOB")
    private byte[] embedding;
    private LocalDate releaseDate;
    private LocalDate createdAt;
    @Column(columnDefinition = "TEXT")
    private String thumbnail;
    private Long likeCount;
    // 정렬을 위해 일단 임시로...
    @LastModifiedDate
    protected LocalDate modifiedAt=LocalDate.now();
}
