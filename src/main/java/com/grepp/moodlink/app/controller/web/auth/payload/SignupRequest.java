package com.grepp.moodlink.app.controller.web.auth.payload;

import com.grepp.moodlink.app.model.member.dto.MemberDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank
    private String userId;

    @NotBlank
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자리를 입력해주세요.")
    private String password;

    @NotBlank
    private String username;

    @NotBlank
    private String genre;

    @NotBlank
    private String periods;

    @NotBlank
    private String countries;

    public MemberDto toDto() {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserId(userId);
        memberDto.setPassword(password);
        memberDto.setUsername(username);
        memberDto.setGenre(genre);
        memberDto.setPeriods(periods);
        memberDto.setCountries(countries);
        memberDto.setCreatedAt(LocalDate.now());
        return memberDto;
    }
}