package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.imgbb.ImgUploadTemplate;
import com.grepp.moodlink.infra.response.ResponseCode;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class MusicService {

    private final MusicRepository musicRepository;
    private final ModelMapper mapper;
    private final ImgUploadTemplate imgUploadTemplate;

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

    public Page<MusicDto> findPaged(Pageable pageable) {
        return musicRepository.findPaged(pageable)
            .map(e -> mapper.map(e, MusicDto.class));
    }

    @Transactional
    public void addMusic(List<MultipartFile> thumbnail, MusicDto dto) {

        if(musicRepository.existsByTitleAndSinger(dto.getTitle(),dto.getSinger()))
            throw new CommonException(ResponseCode.DUPLICATED_DATA);

        try {

            uploadImage(thumbnail, dto);
            Music music = mapper.map(dto, Music.class);

            long count = musicRepository.count();
            music.setId("S"+count);

            log.info("{}",music);

            musicRepository.save(music);
        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void uploadImage(List<MultipartFile> thumbnail, MusicDto dto) throws IOException {
        if(thumbnail != null){
            MultipartFile file =  thumbnail.getFirst();
            String originFileName = file.getOriginalFilename();
            if (originFileName != null && originFileName.contains(".")) {
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String renameFileName = UUID.randomUUID().toString() + ext;

                String thumbnailUrl = imgUploadTemplate.uploadImage(file, renameFileName);
                dto.setThumbnail(thumbnailUrl);
            }
        }
    }

    public MusicDto findById(String id) {
        return musicRepository.findById(id).map(e -> mapper.map(e, MusicDto.class)).orElse(null);
    }

    public void updateMusic(List<MultipartFile> thumbnail, MusicDto dto) {

        try {
           uploadImage(thumbnail,dto);
            // 업데이트
            musicRepository.updateBook(dto);

            log.info("{}",dto);


        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Transactional
    public void deleteMusic(String id) {
        musicRepository.findById(id).ifPresent(Music::unActivated);
    }

    public List<String> parseRecommend(String musicResult) {
        List<String> result = new ArrayList<>();
        if (musicResult == null || musicResult.isBlank()) return result;

        String line = musicResult.trim().replaceFirst("^[가-힣a-zA-Z0-9\\s:]+", "");

        Matcher m = Pattern.compile("\"([^\"]+)\"").matcher(line);
        while (m.find()) {
            String title = m.group(1).trim();
            if (title.startsWith("[") && title.endsWith("]")) {
                title = title.substring(1, title.length()-1).trim();
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
}
