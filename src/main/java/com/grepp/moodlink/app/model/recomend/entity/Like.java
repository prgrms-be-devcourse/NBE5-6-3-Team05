package com.grepp.moodlink.app.model.recomend.entity;


import com.grepp.moodlink.app.model.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Id
    private String id;

    private String userId;
}