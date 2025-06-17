package com.grepp.moodlink.app.controller.web.member.payload;


import com.grepp.moodlink.app.model.member.dto.ModifyDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ModifyRequest {

    private String username;
    private String currentPassword;
    private String newPassword;
    private String genre;
    private String periods;
    private String countries;


    public ModifyDto toDto() {
        return new ModifyDto(username, currentPassword, newPassword, genre, periods, countries);
    }

}
