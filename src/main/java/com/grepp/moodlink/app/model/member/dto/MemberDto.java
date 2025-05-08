package com.grepp.moodlink.app.model.member.dto;

import com.grepp.moodlink.app.model.auth.code.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MemberDto {
    private String userId;
    private String password;
    private String email;
    private Role role;
    private String tel;
    private MemberInfoDto info;
}
