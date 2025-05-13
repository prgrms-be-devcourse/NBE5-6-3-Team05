package com.grepp.moodlink.app.model.song;


import com.grepp.moodlink.app.model.song.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {

}
