package com.grepp.moodlink.app.model.member.dto;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SmtpDto {
    private String from;
    private String subject;
    private List<String> to;
    private Map<String,String> properties;
    private String eventType;
}
