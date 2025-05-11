package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.book.BookRepository;
import com.grepp.moodlink.app.model.book.dto.BookDto;
import com.grepp.moodlink.app.model.book.entity.Book;
import com.grepp.moodlink.app.model.recomend.entity.Like;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailBooks;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {


    private final LikeRepository likeRepository;
    private final LikeDetailMoviesRepository likeDetailMoviesRepository;
    private final LikeDetailBooksRepository likeDetailBooksRepository;
    private final LikeDetailSongRepository likeDetailSongRepository;
    private final BookRepository bookRepository;

    public List<Like> getLikeInfo(String userId) {
        return likeRepository.findByUserId(userId);
    }

    public List<LikeDetailBooks> getLikeDetailBook(List<Like> likes) {
        List<LikeDetailBooks> likeDetailBooks = new ArrayList<>();

        for (Like like : likes) {
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
        List<Like> likes = getLikeInfo(userId);
        List<LikeDetailBooks> likeDetailBooks = getLikeDetailBook(likes);
        return getBookList(likeDetailBooks);
    }


}
