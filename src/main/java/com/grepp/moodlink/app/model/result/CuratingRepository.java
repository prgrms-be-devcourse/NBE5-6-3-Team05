package com.grepp.moodlink.app.model.result;

import com.grepp.moodlink.app.model.result.entity.Curating;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuratingRepository extends JpaRepository<Curating,Long> {
    List<Curating> findByUserId(String UserId);
}
