package com.grepp.moodlink.app.model.data.music;


import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.dto.MusicGenreDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.data.music.entity.MusicGenre;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MusicService {

    private final MusicRepository musicRepository;
    private final ModelMapper mapper;
    private final MusicGenreRepository musicGenreRepository;

    public void saveMusic(List<MusicDto> musicDtos) {

        for (MusicDto dto : musicDtos) {
            Music music = new Music();
            long count = musicRepository.count();
            music.setId("S" + count);
            music.setTitle(dto.getTitle());
            music.setGenre(musicGenreRepository.findById(Long.parseLong(dto.getGenre())).orElseThrow());
            music.setSinger(dto.getSinger());
            music.setDescription(dto.getDescription());
            music.setLyrics(dto.getLyrics());
            music.setReleaseDate(dto.getReleaseDate());
            music.setThumbnail(String.valueOf(dto.getThumbnail()));
            music.setCreatedAt(LocalDate.now());
            music.setLikeCount(0L);

            musicRepository.save(music);
        }
    }

    public List<String> parseRecommend(String musicResult) {
        List<String> result = new ArrayList<>();
        if (musicResult == null || musicResult.isBlank()) {
            return result;
        }

        String line = musicResult.trim().replaceFirst("^[가-힣a-zA-Z0-9\\s:]+", "");

        Matcher m = Pattern.compile("\"([^\"]+)\"").matcher(line);
        while (m.find()) {
            String title = m.group(1).trim();
            if (title.startsWith("[") && title.endsWith("]")) {
                title = title.substring(1, title.length() - 1).trim();
            }
            result.add(title);
        }
        return result.stream()
            .map(musicRepository::findIdByTitle)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .distinct()
            .collect(Collectors.toList());
    }

    public MusicDto findById(String id) {
        return musicRepository.findById(id).map(e -> mapper.map(e, MusicDto.class)).orElse(null);
    }

    @Transactional
    public void incrementLikeCount(String id) {
        Music music = musicRepository.findById(id).orElseThrow();
        Long currentCount = music.getLikeCount();
        music.setLikeCount(currentCount + 1);
    }

    @Transactional
    public void decreaseLikeCount(String id) {
        Music music = musicRepository.findById(id).orElseThrow();
        Long currentCount = music.getLikeCount();
        music.setLikeCount(currentCount - 1);
    }

    public MusicGenreDto getMusicGenre(String id){
        Music music = musicRepository.findById(id).orElseThrow();
        MusicGenre genre = music.getGenre();
        return new MusicGenreDto(genre.getId(), genre.getName());
    }

}
