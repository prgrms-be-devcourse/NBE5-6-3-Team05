package com.grepp.moodlink.app.model.result.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuratingDetailIdDto {
    private String movieId;
    private String songId;
    private String bookId;
}
