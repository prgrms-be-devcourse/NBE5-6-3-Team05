package com.grepp.moodlink.app.controller.web.worldcup.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorldcupResultResponse {
    private Long totalCount;
    private Long winCount;
}
