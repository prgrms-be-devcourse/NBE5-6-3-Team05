package com.grepp.moodlink.app.model.member.dto;



import com.grepp.moodlink.app.model.member.entity.Member;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class MemberInfoDto {

        @Id
        private String id;


        private String username;
        private String role = "ROLE_USER";
        private LocalDate createdAt;
        private LocalDate updatedAt;
        private String countries;

    public static MemberInfoDto ToDto(Member user) {
        MemberInfoDto dto = new MemberInfoDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setCountries(user.getCountries());

        return dto;

    }



    }
