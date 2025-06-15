package com.grepp.moodlink.app.controller.api.worldcup.payload;

import com.grepp.moodlink.app.model.worldcup.code.ContentType;
import com.grepp.moodlink.app.model.worldcup.entity.WorldcupItem;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorldcupPlayResponse {
    public List<WorldcupItem> itemIdList;
    public ContentType contentType;
    public Map<String, Long> finalWinnerCountMap;

}
