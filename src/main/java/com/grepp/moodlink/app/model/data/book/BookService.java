package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
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
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper mapper;
    private final ImgUploadTemplate imgUploadTemplate;

    // 관리자 페이지에서 도서 추가
    @Transactional
    public void addBook(List<MultipartFile> thumbnail, BookDto dto) {

        if(bookRepository.existsByTitleAndAuthor(dto.getTitle(),dto.getAuthor()))
            throw new CommonException(ResponseCode.DUPLICATED_DATA);

        try {
            Book book = mapper.map(dto, Book.class);

            if(thumbnail != null){
                MultipartFile file =  thumbnail.getFirst();
                String originFileName = file.getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String renameFileName = UUID.randomUUID().toString() + ext;

                String thumbnailUrl = imgUploadTemplate.uploadImage(thumbnail.getFirst(), renameFileName);
                book.setImage(thumbnailUrl);
            }

            long count = bookRepository.count();
            book.setIsbn("B"+count);

            log.info("{}",book);

            bookRepository.save(book);
        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    public void saveMusic(List<BookDto> bookDtos) {

        for (BookDto dto : bookDtos) {
            Book book = new Book();
            long count = bookRepository.count();
            book.setIsbn("B" + count);
            book.setTitle(dto.getTitle());
            book.setImage(String.valueOf(dto.getImage()));
            book.setAuthor(dto.getAuthor());
            book.setPublisher(dto.getPublisher());
            book.setPublishedDate(dto.getPublishedDate());
            book.setDescription(dto.getDescription());
            book.setGenre(dto.getGenre());

            bookRepository.save(book);
        }
    }


    public List<BookDto> findByActivated() {
        return bookRepository.findByActivated(true)
            .stream().map(e -> mapper.map(e, BookDto.class))
            .toList();
    }

    public Page<BookDto> findPaged(Pageable pageable) {
        return bookRepository.findPaged(pageable)
            .map(e -> mapper.map(e, BookDto.class));
    }


    public BookDto findByIsbn(String isbn) {
        return mapper.map(bookRepository.findByIsbn(isbn), BookDto.class);
    }

    public void updateBook(List<MultipartFile> thumbnail, BookDto dto) {

        try {

            if(thumbnail != null){
                MultipartFile file =  thumbnail.getFirst();
                String originFileName = file.getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String renameFileName = UUID.randomUUID().toString() + ext;

                String thumbnailUrl = imgUploadTemplate.uploadImage(thumbnail.getFirst(), renameFileName);
                dto.setImage(thumbnailUrl);
            }

            // 업데이트
            bookRepository.updateBook(dto);

            log.info("{}",dto);


        } catch (IOException e) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Transactional
    public void deleteBook(String isbn) {
        bookRepository.findByIsbn(isbn).unActivated();
    }

    public List<Book> parseRecommend(String bookResult) {
        return null;
    }
}
