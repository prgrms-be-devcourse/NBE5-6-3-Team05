package com.grepp.moodlink.app.model.recomend.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class LikeDetailBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long likesId;
    private LocalDateTime createdAt;
}
