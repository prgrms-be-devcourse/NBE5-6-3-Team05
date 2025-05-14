package com.grepp.moodlink.app.model.like;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikesService {

    // db에서 상태를 가져오자. 현재는 map을 통해 확인중
    private final Map<String, Boolean> likeMovieStatusMap = new ConcurrentHashMap<>();
    private final Map<String, Boolean> likeSongStatusMap = new ConcurrentHashMap<>();
    private final Map<String, Boolean> likeBookStatusMap = new ConcurrentHashMap<>();

    public boolean toggleMovieStatus(String id) {

        // db에서 가져오기 (임의로 false로 초기화)
        boolean current = likeMovieStatusMap.getOrDefault(id, false);
        //
        boolean updated = !current;
        likeMovieStatusMap.put(id,updated);
        System.out.println("movie: "+id+" ,current status = "+updated);

        return updated;
    }

    public boolean toggleSongStatus(String id) {
        // db에서 가져오기 (임의로 false로 초기화)
        boolean current;
        if (id.equals("s1111")){
            current = likeSongStatusMap.getOrDefault(id, true);
            System.out.println(current);
        }
        else{
            current = likeSongStatusMap.getOrDefault(id, false);
        }
        //
        boolean updated = !current;
        likeSongStatusMap.put(id,updated);
        System.out.println("song: "+id+" ,current status = "+updated);

        return updated;
    }

    public boolean toggleBookStatus(String id) {
        // db에서 가져오기 (임의로 false로 초기화)
        boolean current = likeBookStatusMap.getOrDefault(id, false);
        //
        boolean updated = !current;
        likeBookStatusMap.put(id,updated);
        System.out.println("book : "+id+" ,current status = "+updated);

        return updated;
    }

    public boolean getBookStatus(String id) {
        return likeBookStatusMap.getOrDefault(id,false);
    }

    public boolean getSongStatus(String id) {

        if (id.equals("s1111")){
            return likeSongStatusMap.getOrDefault(id, true);
        }
        else {
            return likeSongStatusMap.getOrDefault(id, false);
        }
    }

    public boolean getMovieStatus(String id) {
        return likeMovieStatusMap.getOrDefault(id, false);
    }

    @PostConstruct
    public void initDefaultLikes(){
        likeSongStatusMap.put("s1111",true);
    }

}
