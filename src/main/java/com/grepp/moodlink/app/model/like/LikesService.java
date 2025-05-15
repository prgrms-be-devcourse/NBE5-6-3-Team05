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
