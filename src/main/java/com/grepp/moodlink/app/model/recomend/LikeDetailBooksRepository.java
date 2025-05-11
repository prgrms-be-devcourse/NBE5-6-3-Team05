package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.recomend.entity.LikeDetailBooks;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDetailBooksRepository extends JpaRepository<LikeDetailBooks,String> {
    LikeDetailBooks findByLikeId(String likeId);

}
