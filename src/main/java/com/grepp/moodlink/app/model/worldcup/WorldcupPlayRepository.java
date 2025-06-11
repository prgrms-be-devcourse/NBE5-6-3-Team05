package com.grepp.moodlink.app.model.worldcup;

import com.grepp.moodlink.app.model.worldcup.entity.WorldcupPlay;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldcupPlayRepository extends JpaRepository<WorldcupPlay,Long> {

    List<WorldcupPlay> findAllByWorldcupId(Long worldcupId);

    List<WorldcupPlay> findAllByWorldcupIdAndWinnerId(Long worldcupId,String winnerId);
}
