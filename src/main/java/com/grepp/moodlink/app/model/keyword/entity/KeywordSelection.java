package com.grepp.moodlink.app.model.keyword.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "keyword_selection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String keywords;
    @Column(columnDefinition = "BLOB")
    private byte[] embedding;
    private String reason;
}
