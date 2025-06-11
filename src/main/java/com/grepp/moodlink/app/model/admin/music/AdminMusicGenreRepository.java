package com.grepp.moodlink.app.model.admin.music;

import com.grepp.moodlink.app.model.data.music.entity.MusicGenre;
import java.util.List;
import java.util.zip.ZipFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMusicGenreRepository extends JpaRepository<MusicGenre, Long> {

    MusicGenre findByName(String name);

    List<MusicGenre> findAllByActivated(Boolean activated);
}
