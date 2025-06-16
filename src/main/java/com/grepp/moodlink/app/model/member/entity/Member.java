package com.grepp.moodlink.app.model.member.entity;

import com.grepp.moodlink.app.model.auth.code.Role;
import com.grepp.moodlink.app.model.keyword.entity.KeywordSelection;
import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Entity
@Table(name = "user")
@Builder
@Setter // MemberService modifyProfile() 에서 빌더패턴 적용이 잘 안되어서 일단 남겨둠
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    private String id;
    private String password;
    @ManyToOne
    @JoinColumn(name = "KEYWORD_SELECTION_ID")
    private KeywordSelection keywordSelection;
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
