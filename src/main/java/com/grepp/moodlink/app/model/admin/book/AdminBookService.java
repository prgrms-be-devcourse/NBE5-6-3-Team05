package com.grepp.moodlink.app.model.admin.book;

import com.grepp.moodlink.app.model.data.book.BookGenreRepository;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.book.entity.BookGenre;
import com.grepp.moodlink.app.model.llm.EmbeddingService;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.imgbb.ImgUploadTemplate;
import com.grepp.moodlink.infra.response.ResponseCode;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminBookService {

    private final AdminBookRepository adminBookRepository;
    private final EmbeddingService embeddingService;
    private final ImgUploadTemplate imgUploadTemplate;
    private final BookGenreRepository bookGenreRepository;

    // 관리자 페이지에서 도서 추가
    @Transactional
    public void addBook(List<MultipartFile> thumbnail, BookDto dto) {

        if (adminBookRepository.existsByTitleAndAuthor(dto.getTitle(), dto.getAuthor())) {
            throw new CommonException(ResponseCode.DUPLICATED_DATA);
        }

        try {
            uploadImage(thumbnail, dto);

            Book book = Book.builder()
                    .title(dto.getTitle())
                    .image(dto.getImage())
                    .author(dto.getAuthor())
                    .publisher(dto.getPublisher())
                    .publishedDate(dto.getPublishedDate())
                    .description(dto.getDescription())
                    .genre(bookGenreRepository.findByName(dto.getGenre()))
                    .build();

            long count = adminBookRepository.count();
            book.setIsbn("B" + count);

            log.info("{}", book);

            // 입력한 데이터 저장
            adminBookRepository.save(book);
            // 입력한 데이터를 바탕으로 임베딩 값 생성
            embeddingService.generateEmbeddingsBook();
        } catch (IOException e) {
            throw new CommonException(ResponseCode.BAD_REQUEST, e);
        }
    }

    private void uploadImage(List<MultipartFile> thumbnail, BookDto dto) throws IOException {
        if (thumbnail != null) {
            MultipartFile file = thumbnail.getFirst();
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
            .map(BookDto::toDto);
    }


    public BookDto findByIsbn(String isbn) {
        Book book = adminBookRepository.findByIsbn(isbn);
        if(book==null)
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR);
        return BookDto.toDto(book);
    }

    @Transactional
    public void updateBook(List<MultipartFile> thumbnail, BookDto dto) {

        try {
            Book data = adminBookRepository.findByIsbn(dto.getIsbn());

            String ThumbnailImg = data.getImage();
            if(!thumbnail.getFirst().isEmpty()){
                uploadImage(thumbnail, dto);
                ThumbnailImg = dto.getImage();
            }

            Book book = Book.builder()
                .isbn(dto.getIsbn())
                .title(data.getTitle())
                .image(ThumbnailImg)
                .author(data.getAuthor())
                .publisher(dto.getPublisher())
                .publishedDate(dto.getPublishedDate())
                .description(dto.getDescription())
                .genre(bookGenreRepository.findByName(dto.getGenre()))
                .likeCount(data.getLikeCount())
                .build();

            // 업데이트
            adminBookRepository.save(book);
            embeddingService.generateEmbeddingsBook();

        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Transactional
    public void deleteBook(String isbn) {
        adminBookRepository.findByIsbn(isbn).unActivated();
    }

    public List<String> findAllGenre() {
        return bookGenreRepository.findAll().stream().map(BookGenre::getName).toList();
    }
}
