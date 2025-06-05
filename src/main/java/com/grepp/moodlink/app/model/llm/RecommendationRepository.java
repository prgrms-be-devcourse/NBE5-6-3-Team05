package com.grepp.moodlink.app.model.llm;

import com.grepp.moodlink.app.model.llm.code.ContentType;
import com.grepp.moodlink.app.model.llm.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, String> {

    List<Recommendation> findByKeywordsAndContentType(String keywords, String contentType);

}
