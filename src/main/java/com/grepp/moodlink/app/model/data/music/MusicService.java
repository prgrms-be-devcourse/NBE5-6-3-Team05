package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.response.ResponseCode;
import com.grepp.moodlink.infra.util.file.FileDto;
import com.grepp.moodlink.infra.util.file.FileUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
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
    private final FileUtil fileUtil;

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
            List<FileDto> fileDtos = fileUtil.upload(thumbnail, "music");
            Music music = mapper.map(dto, Music.class);

            if(!fileDtos.isEmpty()){
                FileDto fileDto = fileDtos.getFirst();
                String renameFileName = fileDto.renameFileName();
                String savePath = fileDto.savePath();

                music.setThumbnail("/download/" + savePath + renameFileName);
            }

            long count = musicRepository.count();
            music.setId("S"+count);

            log.info("{}",music);

            musicRepository.save(music);
        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    public MusicDto findById(String id) {
        return musicRepository.findById(id).map(e -> mapper.map(e, MusicDto.class)).orElse(null);
    }

    public void updateMusic(List<MultipartFile> thumbnail, MusicDto dto) {

        try {
            List<FileDto> fileDtos = fileUtil.upload(thumbnail, "music");

            if(!fileDtos.isEmpty()){
                FileDto fileDto = fileDtos.getFirst();
                String renameFileName = fileDto.renameFileName();
                String savePath = fileDto.savePath();

                dto.setThumbnail("/download/" + savePath + renameFileName);
            }

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
}
