package com.grepp.moodlink.app.model.keyword;

import com.grepp.moodlink.app.model.keyword.entity.KeywordSelection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public void generateKeywordSelection(String userId) {
        if (keywordRepository.findByUserId(userId) != null) {
            return;
        }

        KeywordSelection keywordSelection = new KeywordSelection();
        keywordSelection.setUserId(userId);
        keywordRepository.save(keywordSelection);
    }
}
