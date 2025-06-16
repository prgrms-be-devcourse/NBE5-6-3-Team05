package com.grepp.moodlink.app.model.llm;

import com.grepp.moodlink.app.model.llm.entity.Recommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationScheduler {
    private final RecommendationRepository recommendationRepository;

    @Scheduled(cron = "0 2 16 * * ?")
    @Transactional
    public void deactivateOldRecommendations() {
        LocalDate cutoff = LocalDate.now().minusDays(1);
        List<Recommendation> oldRecommendations = recommendationRepository.findByCreatedAtBeforeAndActivatedTrue(cutoff);
        System.out.println("오래된 컨텐츠 가져오기");
        for (Recommendation rec : oldRecommendations) {
            rec.setActivated(false);
        }

        recommendationRepository.saveAll(oldRecommendations);
    }
}
