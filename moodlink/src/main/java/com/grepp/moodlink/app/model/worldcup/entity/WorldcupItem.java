package com.grepp.moodlink.app.model.worldcup.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "worldcup_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorldcupItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long worldcupId;
    private String contentId;
    @Builder.Default
    private Long winCount = 0L;
    @Builder.Default
    private Long totalCount = 0L;
}
