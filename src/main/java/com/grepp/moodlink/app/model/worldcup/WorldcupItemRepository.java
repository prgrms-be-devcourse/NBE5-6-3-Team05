package com.grepp.moodlink.app.model.worldcup;

import com.grepp.moodlink.app.model.worldcup.entity.WorldcupItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldcupItemRepository extends JpaRepository<WorldcupItem, Long> {

    List<WorldcupItem> findByWorldcupId(Long id);

    WorldcupItem findWorldcupItemByContentIdAndWorldcupId(String contentId, Long worldcupId);

}
