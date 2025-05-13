package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.controller.web.member.payload.LikeGenreResponse;
import com.grepp.moodlink.app.model.book.BookRepository;
import com.grepp.moodlink.app.model.book.dto.BookDto;
import com.grepp.moodlink.app.model.book.entity.Book;
import com.grepp.moodlink.app.model.recomend.entity.Likes;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailBooks;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {


    private final LikeRepository likeRepository;
    private final LikeDetailMoviesRepository likeDetailMoviesRepository;
    private final LikeDetailBooksRepository likeDetailBooksRepository;
    private final LikeDetailSongRepository likeDetailSongRepository;
    private final BookRepository bookRepository;

    public List<Likes> getLikeInfo(String userId) {
        return likeRepository.findByUserId(userId);
    }

    public List<LikeDetailBooks> getLikeDetailBook(List<Likes> likes) {
        List<LikeDetailBooks> likeDetailBooks = new ArrayList<>();

        for (Likes like : likes) {
            LikeDetailBooks detailBook = likeDetailBooksRepository.findByLikeId(like.getId());

            if (detailBook != null) {
                likeDetailBooks.add(detailBook);
            }
        }

        return likeDetailBooks;

    }

    public List<BookDto> getBookList(List<LikeDetailBooks> likeDetailBooks) {
        List<BookDto> books = new ArrayList<>();

        for (LikeDetailBooks likeDetailBook : likeDetailBooks) {
            Book book = bookRepository.findById(likeDetailBook.getBookId()).orElse(null);

            if (book != null) {
                books.add(BookDto.toDto(book));
            }
        }
        return books;
    }


    public List<BookDto> getUserLikedBooks(String userId) {
        List<Likes> likes = getLikeInfo(userId);
        List<LikeDetailBooks> likeDetailBooks = getLikeDetailBook(likes);
        return getBookList(likeDetailBooks);
    }



    public List<LikeGenreResponse> getPersonalLikeBookGenre(String userId) {
        List<BookDto> books = getUserLikedBooks(userId);

        Map<String, Long> genreCount = new HashMap<>();

        for (BookDto book : books) {
            String genre = book.getGenre();
            if (genre != null && !genre.isBlank()) {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0L) + 1);
            }
        }

        return genreCount.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(6)
            .map(entry -> new LikeGenreResponse(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Transactional
    public List<LikeGenreResponse> getMostLikeBookGenre() {

        List<Book> books = bookRepository.findAll();
        Map<String, Long> genreCount = new HashMap<>();

        for (Book book : books) {
            String genre = book.getGenre();
            Long likeCount = book.getLikeCount();
            if (genre != null && !genre.isBlank()) {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0L) + likeCount);
            }
        }

        return genreCount.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(6)
            .map(entry -> new LikeGenreResponse(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }
}



