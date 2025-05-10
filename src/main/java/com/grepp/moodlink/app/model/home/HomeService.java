package com.grepp.moodlink.app.model.home;

import com.grepp.moodlink.app.model.data.book.BookRepository;
import com.grepp.moodlink.app.model.data.movie.MovieRepository;
import com.grepp.moodlink.app.model.data.music.MusicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final MovieRepository movieRepository;
    private final MusicRepository musicRepository;
    private final BookRepository bookRepository;


    public List<String> showContent() {
        List<String> thumbnail;
        String movie = movieRepository.findById(1L).get().getTitle();
        thumbnail.add();
        return null;
    }
}
