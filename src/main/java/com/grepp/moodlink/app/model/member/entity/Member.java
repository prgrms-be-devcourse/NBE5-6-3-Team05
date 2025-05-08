package com.grepp.moodlink.app.model.member.entity;

import com.grepp.moodlink.app.model.auth.code.Role;
import com.grepp.moodlink.infra.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
public class Member extends BaseEntity {
    
    @Id
    private String userId;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String tel;
    
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "userId")
    private MemberInfo info;
    
    public void updateLoginedAt(LocalDateTime time){
        info.setLoginDate(time);
    }
}
