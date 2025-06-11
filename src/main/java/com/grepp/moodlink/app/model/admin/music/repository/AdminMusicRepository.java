package com.grepp.moodlink.app.model.admin.music.repository;

import com.grepp.moodlink.app.model.data.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMusicRepository extends JpaRepository<Music, String>,
    AdminMusicRepositoryCustom {

    boolean existsByTitleAndSinger(String title, String singer);

    Long countMusicByGenre_Id(Long genreId);
}
