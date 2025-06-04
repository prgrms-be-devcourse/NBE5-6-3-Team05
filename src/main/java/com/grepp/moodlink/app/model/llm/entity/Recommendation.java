package com.grepp.moodlink.app.model.llm.entity;

import com.grepp.moodlink.infra.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "Recommendation",
        uniqueConstraints = @UniqueConstraint(columnNames = {"keywords", "contentType", "contentId"}),
        indexes = {
                @Index(name = "idx_keywords_content_type", columnList = "keywords, contentType")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String keywords;
    private String reason;
    private String contentType;
    private String contentId;
}
