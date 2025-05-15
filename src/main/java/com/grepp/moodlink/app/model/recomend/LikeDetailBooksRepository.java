package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.recomend.entity.LikeDetailBooks;
import feign.Param;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDetailBooksRepository extends JpaRepository<LikeDetailBooks, String> {

    List<LikeDetailBooks> findAllByLikesIdIn(Collection<String> likesIds);
    @Query("SELECT l FROM LikeDetailBooks l WHERE l.likes.id IN :likesIds")
    Page<LikeDetailBooks> findAllByLikesIdInPagination(@Param("likesIds") Collection<String> likesIds, Pageable pageable);
}



