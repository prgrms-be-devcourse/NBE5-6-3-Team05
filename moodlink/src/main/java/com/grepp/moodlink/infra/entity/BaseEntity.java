package com.grepp.moodlink.infra.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    protected Boolean activated = true;

    @CreatedDate
    protected LocalDate createdAt = LocalDate.now();
    @LastModifiedDate
    protected LocalDate modifiedAt;

    public void unActivated() {
        this.activated = false;
    }

}
