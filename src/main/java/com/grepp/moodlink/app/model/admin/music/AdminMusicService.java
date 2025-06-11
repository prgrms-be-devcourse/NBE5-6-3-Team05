package com.grepp.moodlink.app.model.admin.music;

import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.dto.MusicGenreDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.data.music.entity.MusicGenre;
import com.grepp.moodlink.app.model.llm.EmbeddingService;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.imgbb.ImgUploadTemplate;
import com.grepp.moodlink.infra.response.ResponseCode;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
public class AdminMusicService {

    private final AdminMusicRepository musicRepository;
    private final EmbeddingService embeddingService;
    private final ImgUploadTemplate imgUploadTemplate;
    private final AdminMusicGenreRepository musicGenreRepository;
    private final ModelMapper mapper;

    // 음악 목록을 페이지네이션으로 가져옴
    public Page<MusicDto> findPaged(Pageable pageable) {
        return musicRepository.findPaged(pageable)
            .map(MusicDto::toDto);
    }

    // 관리자가 직접 음악을 추가할 때 쓰이는 메소드
    @Transactional
    public void addMusic(List<MultipartFile> thumbnail, MusicDto dto) {

        if (musicRepository.existsByTitleAndSinger(dto.getTitle(), dto.getSinger())) {
            throw new CommonException(ResponseCode.DUPLICATED_DATA);
        }

        try {

            uploadImage(thumbnail, dto);

            Music music = Music.builder()
                .thumbnail(dto.getThumbnail())
                .title(dto.getTitle())
                .genre(musicGenreRepository.findByName((dto.getGenre())))
                .singer(dto.getSinger())
                .description(dto.getDescription())
                .lyrics(dto.getLyrics())
                .releaseDate(dto.getReleaseDate())
                .likeCount(0L)
                .build();

            long count = musicRepository.count();
            music.setId("S" + count);

            log.info("{}", music);

            // 입력한 데이터 추가
            musicRepository.save(music);
            // 입력한 데이터를 바탕으로 임베딩 값 생성
            embeddingService.generateEmbeddingsMusic();
        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    // 추가, 수정 시에 이미지를 업로드 하는 메소드
    private void uploadImage(List<MultipartFile> thumbnail, MusicDto dto) throws IOException {
        if (thumbnail != null) {
            MultipartFile file = thumbnail.getFirst();
            String originFileName = file.getOriginalFilename();
            if (originFileName != null && originFileName.contains(".")) {
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String renameFileName = UUID.randomUUID().toString() + ext;

                String thumbnailUrl = imgUploadTemplate.uploadImage(file, renameFileName);
                dto.setThumbnail(thumbnailUrl);
            }
        }
    }

    // 수정 시에 id를 통해 기존에 저장되어 있던 정보를 가져옴
    public MusicDto findById(String id) {
        Optional<Music> music = musicRepository.findById(id);
        if(music.isEmpty())
            throw new CommonException(ResponseCode.BAD_REQUEST);
        return music.map(MusicDto::toDto).orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));
    }


    // 관리자가 직접 음악 데이터를 수정
    @Transactional
    public void updateMusic(List<MultipartFile> thumbnail, MusicDto dto) {

        try {
            Music data = musicRepository.findById(dto.getId()).orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));
            String ThumbnailImg = data.getThumbnail();
            if(!thumbnail.getFirst().isEmpty()){
                uploadImage(thumbnail, dto);
                ThumbnailImg = dto.getThumbnail();
            }

            Music music = Music.builder()
                .id(dto.getId())
                .thumbnail(ThumbnailImg)
                .title(data.getTitle())
                .genre(musicGenreRepository.findByName(dto.getGenre()))
                .singer(data.getSinger())
                .description(dto.getDescription())
                .lyrics(dto.getLyrics())
                .releaseDate(dto.getReleaseDate())
                .likeCount(data.getLikeCount())
                .build();
            // 업데이트
            musicRepository.save(music);
            embeddingService.generateEmbeddingsMusic();

        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    // 음악 삭제 (소프트 드랍)
    @Transactional
    public void deleteMusic(String id) {
        musicRepository.findById(id).ifPresent(Music::unActivated);
    }
    
    // 모든 음악 장르를 가져옴
    public List<String> findAllGenreName() {
        return musicGenreRepository.findAll().stream().map(MusicGenre::getName).toList();
    }

    public List<MusicGenreDto> findAllGenre() {
        return musicGenreRepository.findAllByActivated(true).stream().map(e->mapper.map(e,MusicGenreDto.class)).collect(
            Collectors.toList());
    }

    // 해당 장르를 사용하는 음악 수 카운트
    public Long countMusicByGenre(Long id) {
        return musicRepository.countMusicByGenre_Id(id);
    }

    // 장르 추가
    @Transactional
    public void addGenre(MusicGenreDto musicGenreDto) {
        if(musicGenreRepository.findByName(musicGenreDto.getName())!=null){
            throw new CommonException(ResponseCode.DUPLICATED_DATA);
        }
        MusicGenre musicGenre = new MusicGenre(null, musicGenreDto.getName());
        musicGenreRepository.save(musicGenre);
    }

    // 장르 삭제
    @Transactional
    public Boolean deleteGenre(Long id) {
        // 해당 장르인 음악이 하나라도 있다면 삭제 불가능
        if(countMusicByGenre(id)!=0)
            return false;
        musicGenreRepository.findById(id).ifPresent(MusicGenre::unActivated);
        return true;
    }

    // 장르 수정
    @Transactional
    public void modifyGenre(Long longId, MusicGenreDto musicGenreDto) {

        MusicGenre genre = musicGenreRepository.findById(longId).get();
        genre.setName(musicGenreDto.getName());
        musicGenreRepository.save(genre);

    }
}
