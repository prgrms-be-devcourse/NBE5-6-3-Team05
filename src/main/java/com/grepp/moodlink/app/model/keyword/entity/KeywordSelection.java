package com.grepp.moodlink.app.model.keyword.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "keyword_selection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private LocalDate selectionDate = LocalDate.now();
    private String keywords;
    @Column(columnDefinition = "BLOB")
    private byte[] embedding;
}
