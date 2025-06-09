package com.grepp.moodlink.app.controller.api.worldcup.payload;


import java.util.List;
import lombok.Data;

@Data
public class WorldcupPlayRequest {
    private List<String> tournament;
    private String worldcupId;
}
