package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.response.ResponseCode;
import com.grepp.moodlink.infra.util.file.FileDto;
import com.grepp.moodlink.infra.util.file.FileUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

import static com.grepp.moodlink.app.model.data.movie.MovieService.processEnclosures;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookRepositoryImpl bookRepositoryImpl;
    private final FileUtil fileUtil;
    private final ModelMapper mapper;

    // 관리자 페이지에서 도서 추가
    @Transactional
    public void addBook(List<MultipartFile> thumbnail, BookDto dto) {

        if(bookRepository.existsByTitleAndAuthor(dto.getTitle(),dto.getAuthor()))
            throw new CommonException(ResponseCode.DUPLICATED_DATA);

        try {
            List<FileDto> fileDtos = fileUtil.upload(thumbnail, "book");
            Book book = mapper.map(dto, Book.class);

            if(!fileDtos.isEmpty()){
                FileDto fileDto = fileDtos.getFirst();
                String renameFileName = fileDto.renameFileName();
                String savePath = fileDto.savePath();

                book.setImage("/download/" + savePath + renameFileName);
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

    public void updateBook(List<MultipartFile> image, BookDto dto) {

        try {
            List<FileDto> fileDtos = fileUtil.upload(image, "book");

            if(!fileDtos.isEmpty()){
                FileDto fileDto = fileDtos.getFirst();
                String renameFileName = fileDto.renameFileName();
                String savePath = fileDto.savePath();

                dto.setImage("/download/" + savePath + renameFileName);
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
        List<String> titles = new ArrayList<>();
        Pattern pattern = Pattern.compile("^\\d+\\.\\s*(.*?)\\s*(?=:)", Pattern.MULTILINE);
        String[] lines = bookResult.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String title = processEnclosures(matcher.group(1).trim());
                titles.add(title);
            }
        }

        return titles.stream()
                .map(bookRepository::findByTitle)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());
    }

}
