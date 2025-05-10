package com.grepp.moodlink.app.model.recomend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Id
    private Long id;

    private String userId;
}