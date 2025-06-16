package com.grepp.moodlink.app.model.worldcup;

import com.grepp.moodlink.app.model.worldcup.entity.WorldcupPlay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldcupPlayRepository extends JpaRepository<WorldcupPlay,Long> {



    long countByWinnerIdAndWorldcupId(String contentId, Long worldcupId);
}
