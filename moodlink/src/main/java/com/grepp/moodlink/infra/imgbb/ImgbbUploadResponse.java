package com.grepp.moodlink.infra.imgbb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImgbbUploadResponse {

    private JsonData data;

    @Data
    public static class JsonData {

        @JsonProperty("display_url")
        private String displayUrl;
    }

    public String getDisplayUrl() {
        return data.getDisplayUrl();
    }
}