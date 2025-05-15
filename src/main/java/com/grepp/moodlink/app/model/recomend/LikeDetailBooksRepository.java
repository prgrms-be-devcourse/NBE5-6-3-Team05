package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.recomend.entity.LikeDetailBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDetailBooksRepository extends JpaRepository<LikeDetailBooks, String> {

    LikeDetailBooks findByLikesId(Long likeId);

    void deleteById(Long id);
}
