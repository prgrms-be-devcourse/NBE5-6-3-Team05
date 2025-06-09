package com.grepp.moodlink.app.model.admin.music;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.response.ResponseCode;
import feign.template.UriUtils;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class MusicLookupService {

    @Value("${lookup.genius.api.key}")
    private String geniusApiKey;
    @Value("${lookup.rapid.api.key}")
    private String rapidApiKey;


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MusicDto lookup(String title) throws JsonProcessingException {

        Integer id = getMusicId(title);
        MusicDto music = getMusicDto(id);
        music.setLyrics(getMusicLyrics(id));
        return music;
    }

    private Integer getMusicId(String title) throws JsonProcessingException {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(geniusApiKey);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String baseUrl = "https://api.genius.com/search";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .queryParam("q",title)
            .toUriString();

        ResponseEntity<String> response = restTemplate.exchange(
            requestUrl,
            HttpMethod.GET,
            entity,
            String.class
        );

        // 1) 루트 노드 파싱
        JsonNode root = objectMapper.readTree(response.getBody());
// 2) hits 배열 노드
        JsonNode hits = root.path("response").path("hits");
// 3) hits 가 배열이고, 사이즈가 1 이상인지 체크
        if (!hits.isArray() || hits.isEmpty()) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

// 4) 첫 번째 요소의 result.id 추출
        JsonNode idNode = hits.get(0).path("result").path("id");
        if (idNode.isMissingNode() || !idNode.isInt()) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        return idNode.asInt();
    }

    private MusicDto getMusicDto(Integer id) throws JsonProcessingException {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + geniusApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String baseUrl = "https://api.genius.com/songs/"+id;
        String requestUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .queryParam("text_format","plain")
            .build(false).toUriString();

        ResponseEntity<String> response = restTemplate.exchange(
            requestUrl,
            HttpMethod.GET,
            entity,
            String.class
        );

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode songNode = root
            .path("response")
            .path("song");

        MusicDto music = new MusicDto();
        music.setThumbnail(songNode.path("header_image_thumbnail_url").asText());
        music.setTitle(songNode.path("title").asText());
        music.setReleaseDate(LocalDate.parse(songNode.path("release_date").asText()));
        music.setSinger(songNode.path("artist_names").asText());
        String description = songNode.path("description").path("plain").asText();
        if(!description.isEmpty()) {
            music.setDescription(songNode.path("description").path("plain").asText());
        }
        return music;
    }

    private String getMusicLyrics(Integer id) throws JsonProcessingException {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-host", "genius-song-lyrics1.p.rapidapi.com");
        headers.set("x-rapidapi-key",rapidApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String baseUrl = "https://genius-song-lyrics1.p.rapidapi.com/song/lyrics/?id="+id;
        String requestUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .queryParam("text_format","plain")
            .build(false).toUriString();

        ResponseEntity<String> response = restTemplate.exchange(
            requestUrl,
            HttpMethod.GET,
            entity,
            String.class
        );

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode lyricsNode = root
            .path("lyrics")
            .path("lyrics")
            .path("body").path("plain");

        return lyricsNode.asText();

    }
}
