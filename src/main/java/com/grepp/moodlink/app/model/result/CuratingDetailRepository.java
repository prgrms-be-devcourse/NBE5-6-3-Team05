package com.grepp.moodlink.app.model.result;

import com.grepp.moodlink.app.model.result.entity.CuratingDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuratingDetailRepository extends JpaRepository<CuratingDetail,Long> {

    List<CuratingDetail> findByCuratingId(Long curatingId);
}
