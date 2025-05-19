package com.grepp.moodlink.app.model.keyword.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "keyword_selection_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordSelectionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long keywordSelectionId;

    private String mood;
    private String random1;
    private String answer1;
    private String random2;
    private String answer2;
    private String random3;
    private String answer3;
}
