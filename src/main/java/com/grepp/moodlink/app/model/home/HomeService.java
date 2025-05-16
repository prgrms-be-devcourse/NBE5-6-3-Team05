package com.grepp.moodlink.app.model.home;

import com.grepp.moodlink.app.model.data.ContentDto;
import com.grepp.moodlink.app.model.data.book.BookRepositoryImpl;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.movie.MovieRepositoryImpl;
import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.music.MusicRepositoryImpl;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.member.MemberRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final MusicRepositoryImpl musicRepositoryImpl;
    private final MovieRepositoryImpl movieRepositoryImpl;
    private final BookRepositoryImpl bookRepositoryImpl;
    private final MemberRepositoryImpl memberRepositoryImpl;


    public List<String> showContent() {
        List<String> thumbnail = new ArrayList<>();

        String music = musicRepositoryImpl.findTopThumbnail();
        String movie = movieRepositoryImpl.findTopThumbnail();
        String book = bookRepositoryImpl.findTopThumbnail();

        thumbnail.add(music);
        thumbnail.add(movie);
        thumbnail.add(book);

        return thumbnail;

    }

    public String findUsername(String userId) {
        return memberRepositoryImpl.findUsernameById(userId);
    }

    public List<String> findPeople() {
        List<String> people = new ArrayList<>();

        String music = musicRepositoryImpl.findPeople();
        String book = bookRepositoryImpl.findPeople();

        people.add(music);
        people.add(book);

        return people;
    }

    public List<String> findTitle() {
        List<String> title = new ArrayList<>();

        String music = musicRepositoryImpl.findTitle();
        String movie = movieRepositoryImpl.findTitle();
        String book = bookRepositoryImpl.findTitle();

        title.add(music);
        title.add(movie);
        title.add(book);

        return title;
    }

    public List<? extends ContentDto> searchContent(String contentName) {
        List<MusicDto> music = musicRepositoryImpl.searchContent(contentName);
        System.out.println("음악 검색 결과: " + music.size());
        if (!music.isEmpty()) return music;

        List<MovieDto> movie = movieRepositoryImpl.searchContent(contentName);
        System.out.println("영화 검색 결과: " + movie.size());
        if (!movie.isEmpty()) return movie;

        List<BookDto> book = bookRepositoryImpl.searchContent(contentName);
        System.out.println("책 검색 결과: " + book.size());
        return book;
    }
}
