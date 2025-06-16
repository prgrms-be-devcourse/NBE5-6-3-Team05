package com.grepp.moodlink.app.controller.api.worldcup.payload;

import java.util.List;
import lombok.Data;

@Data
public class WorldcupIdsRequest {
    private List<String> id;
    private String title;
    private String contentType;
    private String image;
}
