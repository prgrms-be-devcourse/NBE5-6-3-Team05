package com.grepp.moodlink.app.controller.web.admin.payload;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MovieAddRequest {

    private List<MultipartFile> thumbnail;
    @NotBlank
    private String title;
    @NotBlank
    private String director;
    @NotBlank
    private String genre;

    private String description;

    private LocalDate releaseDate;
}
