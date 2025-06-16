package com.grepp.moodlink.app.controller.web.member.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeGenreResponse {

    private String genre;
    private Long count;

}
