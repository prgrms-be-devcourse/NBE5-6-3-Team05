package com.grepp.moodlink.infra.event;

import com.grepp.moodlink.infra.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Outbox extends BaseEntity {

    @Id
    private String eventId = UUID.randomUUID().toString();
    private String eventType;
    private String sourceService = "moodlink";
    private String payload;

    public Outbox(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
    }
}
