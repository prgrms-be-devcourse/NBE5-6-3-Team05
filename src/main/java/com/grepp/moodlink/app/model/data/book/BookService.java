package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper mapper;

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

    public List<String> parseRecommend(String movieResult) {
        List<String> result = new ArrayList<>();
        if (movieResult == null || movieResult.isBlank()) {
            return result;
        }

        String line = movieResult.trim().replaceFirst("^[가-힣a-zA-Z0-9\\s:]+", "");

        Matcher m = Pattern.compile("\"([^\"]+)\"").matcher(line);
        while (m.find()) {
            String title = m.group(1).trim();
            if (title.startsWith("[") && title.endsWith("]")) {
                title = title.substring(1, title.length() - 1).trim();
            }
            result.add(title);
        }
        return result.stream()
            .map(bookRepository::findIsbnByTitle)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .distinct()
            .collect(Collectors.toList());
    }

    public BookDto findByIsbn(String isbn) {
        return mapper.map(bookRepository.findByIsbn(isbn), BookDto.class);
    }

    @Transactional
    public void incrementLikeCount(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        Long currentCount = book.getLikeCount();
        book.setLikeCount(currentCount + 1);
    }

    @Transactional
    public void decreaseLikeCount(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        Long currentCount = book.getLikeCount();
        book.setLikeCount(currentCount - 1);
    }
}
