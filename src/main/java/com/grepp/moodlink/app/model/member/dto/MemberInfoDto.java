package com.grepp.moodlink.app.model.member.dto;


import com.grepp.moodlink.app.model.auth.code.Role;
import com.grepp.moodlink.app.model.member.entity.Member;
import java.time.LocalDate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class MemberInfoDto {

    private String id;
    private String username;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String genre;
    private String periods;
    private String countries;

    public static MemberInfoDto ToDto(Member user) {
        MemberInfoDto dto = new MemberInfoDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setCountries(user.getCountries());
        dto.setGenre(user.getGenre());
        dto.setPeriods(user.getPeriods());

        return dto;

    }


}
