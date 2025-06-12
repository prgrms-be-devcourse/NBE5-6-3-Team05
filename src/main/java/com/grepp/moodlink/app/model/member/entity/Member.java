package com.grepp.moodlink.app.model.member.entity;

import com.grepp.moodlink.app.model.auth.code.Role;
import com.grepp.moodlink.app.model.keyword.entity.KeywordSelection;
import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "user")
public class Member {

    @Id
    private String id;
    private String password;
    @ManyToOne
    @JoinColumn(name = "KEYWORD_SELECTION_ID")
    private KeywordSelection keywordSelectionId;
    private String username;
    private String email;
    private String genre;
    private String periods;
    private String countries;

    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
