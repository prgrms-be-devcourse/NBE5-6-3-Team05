package com.grepp.moodlink.app.model.admin.music;

import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.llm.EmbeddingService;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.imgbb.ImgUploadTemplate;
import com.grepp.moodlink.infra.response.ResponseCode;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
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
    private final ModelMapper mapper;
    private final ImgUploadTemplate imgUploadTemplate;

    // 음악 목록을 페이지네이션으로 가져옴
    public Page<MusicDto> findPaged(Pageable pageable) {
        return musicRepository.findPaged(pageable)
            .map(e -> mapper.map(e, MusicDto.class));
    }

    // 관리자가 직접 음악을 추가할 때 쓰이는 메소드
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

    // 수정 시에 id를 통해 기존에 저장되어 있던 정보를 가져옴
    public MusicDto findById(String id) {
        return musicRepository.findById(id).map(e -> mapper.map(e, MusicDto.class)).orElse(null);
    }


    // 관리자가 직접 음악 데이터를 수정
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

    // 음악 삭제 (소프트 드랍)
    @Transactional
    public void deleteMusic(String id) {
        musicRepository.findById(id).ifPresent(Music::unActivated);
    }
}
