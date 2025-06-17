package com.grepp.moodlink.app.model.llm;

import com.grepp.moodlink.app.model.llm.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, String> {

    List<Recommendation> findByKeywordsAndContentTypeAndActivatedTrue(String keywords, String contentType);

    boolean existsByKeywords(String keywords);

    List<Recommendation> findByKeywordsAndActivatedTrue(String keyword);

    List<Recommendation> findByCreatedAtBeforeAndActivatedTrue(LocalDate createdAt);

    List<Recommendation> findByCreatedAtBeforeAndActivatedFalse(LocalDate cutoff);
}
