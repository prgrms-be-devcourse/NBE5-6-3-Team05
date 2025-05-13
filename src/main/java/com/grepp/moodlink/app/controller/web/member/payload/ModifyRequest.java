package com.grepp.moodlink.app.controller.web.member.payload;


import com.grepp.moodlink.app.model.member.dto.ModifyDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ModifyRequest {


    private String password;
    private String username;


    public ModifyDto toDto() {
        return new ModifyDto(username, password);
    }

}
