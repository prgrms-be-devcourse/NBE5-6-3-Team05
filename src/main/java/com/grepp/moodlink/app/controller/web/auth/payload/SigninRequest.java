package com.grepp.moodlink.app.controller.web.auth.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {

    @NotBlank
    private String userId;

    @NotBlank
    @Size(min = 8, max = 15)
    private String password;

}
