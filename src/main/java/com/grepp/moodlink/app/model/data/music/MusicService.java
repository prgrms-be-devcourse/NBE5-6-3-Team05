package com.grepp.moodlink.app.model.data.music;


import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.dto.MusicWorldcupDto;
import com.grepp.moodlink.app.model.data.music.dto.MusicGenreDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.data.music.entity.MusicGenre;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // dto에서의 id의 type을 변경해야 함.
    public List<Map<String, Object>> getMusicList() {
        // Dto 대신 임의의 Map<>을 통해 전달("id" : 음악 컨텐츠의 id, "title": 음악 컨텐츠의 제목)
        return musicRepository.findAll().stream()
            .map(m -> {

                Map<String, Object> map = new HashMap<>();
                map.put("id", m.getId());
                map.put("title", m.getTitle());
                map.put("image", m.getThumbnail());

                // 함수형 내부에서의 return(메서드의 반환이 아님.)
                return map;
            })
            .collect(Collectors.toList());
    }


    public MusicWorldcupDto findDtoById(String id) {
        Music music = musicRepository.findById(id).orElseThrow();

        MusicWorldcupDto dto = new MusicWorldcupDto();
        dto.setId(music.getId());
        dto.setTitle(music.getTitle());
        dto.setImage(music.getThumbnail()); // thumbnail → image 매핑

        return dto;
    }

    // List로 들어온 isbn들로부터 각 Content들의 세부정보(Dto로) 가져오기
    public Map<String, MusicWorldcupDto> getDetails(List<String> ids) {
        return ids.stream()
            .collect(Collectors.toMap(
                id -> id,                    // key
                id -> this.findDtoById(id)   // value
            ));
    }


    public MusicGenreDto getMusicGenre(String id){
        Music music = musicRepository.findById(id).orElseThrow();
        MusicGenre genre = music.getGenre();
        return new MusicGenreDto(genre.getId(), genre.getName());
    }

}
