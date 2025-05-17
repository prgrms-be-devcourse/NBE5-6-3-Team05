package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.recomend.entity.LikeDetailBooks;
import feign.Param;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDetailBooksRepository extends JpaRepository<LikeDetailBooks, Long> {

    List<LikeDetailBooks> findAllByLikesIdIn(Collection<String> likesIds);
    @Query("SELECT l FROM LikeDetailBooks l WHERE l.likesId IN :likesIds")
    Page<LikeDetailBooks> findAllByLikesIdInPagination(@Param("likesIds") Collection<String> likesIds, Pageable pageable);
    List<LikeDetailBooks> findByLikesId(Long likeId);



    @Modifying
    @Transactional
    @Query("DELETE FROM LikeDetailBooks l WHERE l.bookId = :bookId AND l.likesId = :likesId")
    void deleteByBookIdAndLikesId(@Param("bookId") String bookId, @Param("likesId") Long likesId);
}

