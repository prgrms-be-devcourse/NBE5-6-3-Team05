package com.grepp.moodlink.app.controller.web.auth.payload;

import com.grepp.moodlink.app.model.member.dto.MemberDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    private String id;

    @NotBlank
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자리를 입력해주세요.")
    private String password;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String genre;

    @NotBlank
    private String periods;

    @NotBlank
    private String countries;

    public MemberDto toDto() {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(id);
        memberDto.setPassword(password);
        memberDto.setUsername(username);
        memberDto.setEmail(email);
        memberDto.setGenre(genre);
        memberDto.setPeriods(periods);
        memberDto.setCountries(countries);
        memberDto.setCreatedAt(LocalDate.now());
        return memberDto;
    }
}