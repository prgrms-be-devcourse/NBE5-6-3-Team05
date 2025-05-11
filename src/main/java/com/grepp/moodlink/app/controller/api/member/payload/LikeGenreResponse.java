package com.grepp.moodlink.app.controller.api.member.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class LikeGenreResponse {

    private String genre;
    private int count;

}
