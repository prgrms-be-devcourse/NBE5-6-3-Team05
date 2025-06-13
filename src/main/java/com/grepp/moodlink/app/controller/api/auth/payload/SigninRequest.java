package com.grepp.moodlink.app.controller.api.auth.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SigninRequest {
    
    @NotBlank
    private String userId;
    
    @NotBlank
    @Size(min = 8, max = 15)
    private String password;
}
