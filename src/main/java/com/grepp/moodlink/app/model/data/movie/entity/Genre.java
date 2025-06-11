package com.grepp.moodlink.app.model.data.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "genre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    private Boolean activated = true;

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void unActivated() {
        this.activated = false;
    }
}