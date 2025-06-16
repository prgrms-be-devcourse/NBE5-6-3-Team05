package com.grepp.moodlink.app.model.llm.batch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationDto {
    private String keywords;
    private String reason;
    private List<String> movieRecommendations;
    private List<String> bookRecommendations;
    private List<String> musicRecommendations;
}
