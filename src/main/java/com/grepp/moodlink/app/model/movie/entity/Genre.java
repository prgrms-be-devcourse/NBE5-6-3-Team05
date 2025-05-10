package com.grepp.moodlink.app.model.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "genre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre{
    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;
}