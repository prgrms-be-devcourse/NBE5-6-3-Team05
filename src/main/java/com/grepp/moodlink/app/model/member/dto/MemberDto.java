package com.grepp.moodlink.app.model.member.dto;

import com.grepp.moodlink.app.model.auth.code.Role;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDto {

    private String userId;
    private String password;
    private String username;
    private String email;
    private Role role;
    private String genre;
    private String periods;
    private String countries;
    private LocalDate createdAt;
}
