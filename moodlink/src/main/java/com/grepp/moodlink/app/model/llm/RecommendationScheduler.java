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

    @Scheduled(cron = "0 0 0 * * ?")
    public void deactivateOldRecommendations() {
        LocalDate cutoff = LocalDate.now().minusDays(30);
        List<Recommendation> oldRecommendations = recommendationRepository.findByCreatedAtBeforeAndActivatedTrue(cutoff);
        System.out.println("오래된 추천 컨텐츠 비활성화하기");
        System.out.println(cutoff);
        for (Recommendation rec : oldRecommendations) {
            rec.setActivated(false);
        }
        recommendationRepository.saveAll(oldRecommendations);
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void deleteOldRecommendations() {
        LocalDate cutoff = LocalDate.now().minusDays(60);
        List<Recommendation> oldRecommendations = recommendationRepository.findByCreatedAtBeforeAndActivatedFalse(cutoff);
        System.out.println("너무 오래된 추천 컨텐츠 삭제하기");
        recommendationRepository.deleteAll(oldRecommendations);
    }
}
