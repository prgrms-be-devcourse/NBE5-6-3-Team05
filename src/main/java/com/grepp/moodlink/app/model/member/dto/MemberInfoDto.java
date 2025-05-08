package com.grepp.moodlink.app.model.member.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MemberInfoDto {
    private String userId;
    private LocalDateTime loginDate;
    private LocalDateTime modifyDate;
    private LocalDateTime leaveDate;
    private LocalDateTime rentableDate;
}
