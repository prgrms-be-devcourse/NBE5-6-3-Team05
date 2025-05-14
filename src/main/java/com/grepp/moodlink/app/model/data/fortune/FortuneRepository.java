package com.grepp.moodlink.app.model.data.fortune;

import com.grepp.moodlink.app.model.data.fortune.entity.Fortune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FortuneRepository extends JpaRepository<Fortune, Long> {

}
