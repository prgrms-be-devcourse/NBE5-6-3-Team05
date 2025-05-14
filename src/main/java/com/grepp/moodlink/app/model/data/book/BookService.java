package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

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

    public List<Book> parseRecommend(String bookResult) {
        Pattern pattern = Pattern.compile("\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(bookResult);

        List<String> recommendedTitles = new ArrayList<>();
        while (matcher.find()) {
            recommendedTitles.add(matcher.group(1).trim());
        }
        return recommendedTitles.stream()
                .map(title -> bookRepository.findByTitleIgnoreCaseContaining(title.replaceAll("\\s+", " ").trim()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());
    }
}
