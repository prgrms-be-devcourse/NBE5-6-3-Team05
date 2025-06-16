package com.grepp.moodlink.app.model.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ModifyDto {

    private String username;
    private String currentPassword;
    private String newPassword;
    private String genre;
    private String periods;
    private String countries;


}
