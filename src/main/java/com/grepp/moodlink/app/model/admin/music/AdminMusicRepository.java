package com.grepp.moodlink.app.model.admin.music;

import com.grepp.moodlink.app.model.data.music.entity.Music;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMusicRepository extends JpaRepository<Music, String>,
    AdminMusicRepositoryCustom {

    boolean existsByTitleAndSinger(String title, String singer);
}
