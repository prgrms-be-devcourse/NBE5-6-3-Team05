package com.grepp.moodlink.app.model.keyword;

import com.grepp.moodlink.app.model.keyword.entity.KeywordSelection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public String findReason(String keywords) {
        return keywordRepository.findReasonByKeywords(keywords);
    }
}
