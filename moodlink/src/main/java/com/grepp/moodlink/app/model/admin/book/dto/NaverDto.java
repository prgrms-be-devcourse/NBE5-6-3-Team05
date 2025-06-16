package com.grepp.moodlink.app.model.admin.book.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverDto {

    private String lastBuildDate;
    private Integer total;
    private Integer start;
    private Integer display;
    private List<NaverBookDto> items;

}
