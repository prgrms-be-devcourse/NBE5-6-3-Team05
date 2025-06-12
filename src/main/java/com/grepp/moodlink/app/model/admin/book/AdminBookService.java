package com.grepp.moodlink.app.model.admin.book;

import com.grepp.moodlink.app.model.admin.book.repository.AdminBookGenreRepository;
import com.grepp.moodlink.app.model.admin.book.repository.AdminBookRepository;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.dto.BookGenreDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.book.entity.BookGenre;
import com.grepp.moodlink.app.model.llm.EmbeddingService;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.imgbb.ImgUploadTemplate;
import com.grepp.moodlink.infra.response.ResponseCode;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final AdminBookGenreRepository bookGenreRepository;
    private final ModelMapper mapper;

    // 관리자 페이지에서 도서 추가
    @Transactional
    public void addBook(List<MultipartFile> thumbnail, BookDto dto) {

        if (adminBookRepository.existsByTitleAndAuthor(dto.getTitle(), dto.getAuthor())) {
            throw new CommonException(ResponseCode.DUPLICATED_DATA);
        }

        try {
            // api를 통해 가져왔을 때 thumbnail의 path 값을 가져오기에 따로 처리1
            String ThumbnailImg = dto.getImage();
            if(thumbnail!=null&&!thumbnail.getFirst().isEmpty()){
                uploadImage(thumbnail, dto);
                ThumbnailImg = dto.getImage();
            }

            Book book = Book.builder()
                    .title(dto.getTitle())
                    .image(ThumbnailImg)
                    .author(dto.getAuthor())
                    .publisher(dto.getPublisher())
                    .publishedDate(dto.getPublishedDate())
                    .description(dto.getDescription())
                    .genre(bookGenreRepository.findByName(dto.getGenre()))
                    .likeCount(0L)
                    .build();

            long count = adminBookRepository.count();
            book.setIsbn("B" + count);

            log.info("{}", book);

            // 입력한 데이터 저장
            adminBookRepository.save(book);
            // 만약 동시에 접근하여 같은 Id를 가지게 될 경우 방지
            adminBookRepository.flush();
            // 입력한 데이터를 바탕으로 임베딩 값 생성
//            embeddingService.generateEmbeddingsBook();
        } catch (IOException | DataIntegrityViolationException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
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

    public List<String> findAllGenreName() {
        return bookGenreRepository.findAll().stream().map(BookGenre::getName).toList();
    }

    public List<BookGenreDto> findAllGenre() {
        return bookGenreRepository.findAllByActivated(true).stream().map(e->mapper.map(e, BookGenreDto.class)).collect(
            Collectors.toList());
    }

    // 해당 장르를 사용하는 책 수 카운트
    public Long countBooksByGenre(Long id) {
        return adminBookRepository.countBooksByGenre_Id(id);
    }
    
    // 장르 추가
    @Transactional
    public void addGenre(BookGenreDto bookGenreDto) {
        if(bookGenreRepository.findByName(bookGenreDto.getName())!=null){
            throw new CommonException(ResponseCode.DUPLICATED_DATA);
        }
        BookGenre bookGenre = new BookGenre(null, bookGenreDto.getName());
        try{
            bookGenreRepository.save(bookGenre);
            bookGenreRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    // 장르 삭제
    @Transactional
    public Boolean deleteGenre(Long id) {
        // 해당 장르인 책이 하나라도 있다면 삭제 불가능
        if(countBooksByGenre(id)!=0)
            return false;
        bookGenreRepository.findById(id).ifPresent(BookGenre::unActivated);
        return true;
    }
    
    // 장르 수정 
    @Transactional
    public void modifyGenre(Long longId, BookGenreDto bookGenreDto) {
        BookGenre genre = bookGenreRepository.findById(longId).get();
        genre.setName(bookGenreDto.getName());
        bookGenreRepository.save(genre);
    }
}
