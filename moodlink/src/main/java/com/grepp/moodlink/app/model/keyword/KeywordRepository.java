package com.grepp.moodlink.app.model.keyword;

import com.grepp.moodlink.app.model.keyword.entity.KeywordSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordSelection, Long> {

    Optional<KeywordSelection> findByKeywords(String keywords);

    boolean existsByKeywords(String keywords);

}
