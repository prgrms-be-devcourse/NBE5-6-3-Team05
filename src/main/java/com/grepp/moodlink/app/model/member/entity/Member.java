package com.grepp.moodlink.app.model.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    private String id;

    private String password;
    private String username;
    private String role = "ROLE_USER";
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String genre;
    private String periods;
    private String countries;


}


