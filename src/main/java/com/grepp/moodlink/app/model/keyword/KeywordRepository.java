package com.grepp.moodlink.app.model.keyword;

import com.grepp.moodlink.app.model.keyword.entity.KeywordSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordSelection, Long> {

    KeywordSelection findByUserId(String userId);
}
