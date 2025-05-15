package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.grepp.moodlink.app.model.data.movie.MovieService.processEnclosures;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;

    public void saveMusic(List<MusicDto> musicDtos) {

        for (MusicDto dto : musicDtos) {
            Music music = new Music();
            long count = musicRepository.count();
            music.setId("S" + count);
            music.setTitle(dto.getTitle());
            music.setGenre(dto.getGenre());
            music.setSinger(dto.getSinger());
            music.setDescription(dto.getDescription());
            music.setLyrics(dto.getLyrics());
            music.setReleaseDate(dto.getReleaseDate());
            music.setThumbnail(String.valueOf(dto.getThumbnail()));
            music.setCreatedAt(LocalDate.now());
            music.setLikeCount(dto.getLikeCount());

            musicRepository.save(music);
        }
    }

    public List<Music> parseRecommend(String musicResult) {
        List<String> titles = new ArrayList<>();
        Pattern pattern = Pattern.compile("^\\d+\\.\\s*(.*?)\\s*(?=:)", Pattern.MULTILINE);
        String[] lines = musicResult.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String title = processEnclosures(matcher.group(1).trim());
                titles.add(title);
            }
        }
        return titles.stream()
                .map(musicRepository::findByTitle)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());
    }

}
