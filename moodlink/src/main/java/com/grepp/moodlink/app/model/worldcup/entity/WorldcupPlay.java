package com.grepp.moodlink.app.model.worldcup.entity;


import jakarta.persistence.Entity;
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
@Table(name = "worldcup_play")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorldcupPlay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long worldcupId;
    private String userId;
    private String winnerId;
    private LocalDate createdAt;
}
