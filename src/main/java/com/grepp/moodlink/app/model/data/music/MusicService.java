package com.grepp.moodlink.app.model.data.music;

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

        // "노래"로 시작하면 그 부분을 자름
        String line = musicResult.trim();
        if (line.startsWith("노래")) {
            line = line.substring(2).trim();
        }

        // 정규식으로 큰따옴표 안의 텍스트 모두 추출
        Matcher m = Pattern.compile("\"([^\"]+)\"").matcher(line);
        while (m.find()) {
            String title = m.group(1).trim();
            // [제목] 형태 제거 (혹시 있을 경우)
            if (title.startsWith("[") && title.endsWith("]")) {
                title = title.substring(1, title.length() - 1).trim();
            }
            System.out.println(title);
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
}
