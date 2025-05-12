package com.grepp.moodlink.app.model.home;

import com.grepp.moodlink.app.model.data.book.BookRepositoryImpl;
import com.grepp.moodlink.app.model.data.movie.MovieRepositoryImpl;
import com.grepp.moodlink.app.model.data.music.MusicRepositoryImpl;
import com.grepp.moodlink.app.model.member.MemberRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final MovieRepositoryImpl movieRepositoryImpl;
    private final MusicRepositoryImpl musicRepositoryImpl;
    private final BookRepositoryImpl bookRepositoryImpl;
    private final MemberRepositoryImpl memberRepositoryImpl;


    public List<String> showContent() {
        List<String> thumbnail = new ArrayList<>();

        String movie = movieRepositoryImpl.findTopThumbnail();
        String music = musicRepositoryImpl.findTopThumbnail();
        String book = bookRepositoryImpl.findTopThumbnail();

        thumbnail.add(movie);
        thumbnail.add(music);
        thumbnail.add(book);

        return thumbnail;

    }

    public String findUsername(String userId) {
        return memberRepositoryImpl.findUsernameById(userId);
    }
}
