package com.grepp.moodlink.app.model.worldcup.entity;

import com.grepp.moodlink.app.model.worldcup.code.ContentType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "worldcup")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Worldcup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    private String userId;
    private String hashCode;
    private String imageUrl;
    private LocalDate createdAt;
}
