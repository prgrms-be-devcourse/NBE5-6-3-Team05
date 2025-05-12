package com.grepp.moodlink.app.model.details.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SongDetailsDto {
    private String id;
    private String name;
    private String artist;
    private LocalDateTime releasedAt;
    private String description;
    private String songImg;
    private boolean status;
}
