package com.grepp.moodlink.app.model.recomend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like_detail_movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDetailMovies {
    @Id
    private Long id;

    private Long likeId;
    private String movieId;
}
