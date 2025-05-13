package com.grepp.moodlink.app.model.member.entity;

import com.grepp.moodlink.app.model.auth.code.Role;
import com.grepp.moodlink.infra.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class Member extends BaseEntity {

    @Id
    private String id;
    private String password;
    private String username;
    private String genre;
    private String periods;
    private String countries;

    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
