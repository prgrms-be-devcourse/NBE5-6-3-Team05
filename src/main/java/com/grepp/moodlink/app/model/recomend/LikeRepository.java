package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.member.entity.Member;
import com.grepp.moodlink.app.model.recomend.entity.Like;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like,String> {
    List<Like> findByUserId(String userId);

}
