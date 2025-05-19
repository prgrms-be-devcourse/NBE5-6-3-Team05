package com.grepp.moodlink.app.model.admin.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
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


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminBookService {

    private final AdminBookRepository adminBookRepository;
    private final EmbeddingService embeddingService;
    private final ModelMapper mapper;
    private final ImgUploadTemplate imgUploadTemplate;

    // 관리자 페이지에서 도서 추가
    @Transactional
    public void addBook(List<MultipartFile> thumbnail, BookDto dto) {

        if(adminBookRepository.existsByTitleAndAuthor(dto.getTitle(),dto.getAuthor()))
            throw new CommonException(ResponseCode.DUPLICATED_DATA);

        try {
            uploadImage(thumbnail, dto);

            Book book = mapper.map(dto, Book.class);

            long count = adminBookRepository.count();
            book.setIsbn("B"+count);

            log.info("{}",book);

            // 입력한 데이터 저장
            adminBookRepository.save(book);
            // 입력한 데이터를 바탕으로 임베딩 값 생성
            embeddingService.generateEmbeddingsBook();
        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void uploadImage(List<MultipartFile> thumbnail, BookDto dto) throws IOException {
        if(thumbnail != null){
            MultipartFile file =  thumbnail.getFirst();
            String originFileName = file.getOriginalFilename();
            if (originFileName != null && originFileName.contains(".")) {
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String renameFileName = UUID.randomUUID().toString() + ext;

                String thumbnailUrl = imgUploadTemplate.uploadImage(file, renameFileName);
                dto.setImage(thumbnailUrl);
            }
        }
    }

    public Page<BookDto> findPaged(Pageable pageable) {
        return adminBookRepository.findPaged(pageable)
            .map(e -> mapper.map(e, BookDto.class));
    }


    public BookDto findByIsbn(String isbn) {
        return mapper.map(adminBookRepository.findByIsbn(isbn), BookDto.class);
    }

    public void updateBook(List<MultipartFile> thumbnail, BookDto dto) {

        try {
            uploadImage(thumbnail, dto);
            // 업데이트
            adminBookRepository.updateBook(dto);
            embeddingService.generateEmbeddingsBook();
            log.info("{}",dto);


        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Transactional
    public void deleteBook(String isbn) {
        adminBookRepository.findByIsbn(isbn).unActivated();
    }
}
