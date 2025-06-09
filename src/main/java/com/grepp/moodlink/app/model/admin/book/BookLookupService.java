package com.grepp.moodlink.app.model.admin.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.moodlink.app.model.admin.book.dto.NaverBookDto;
import com.grepp.moodlink.app.model.admin.book.dto.NaverDto;
import com.grepp.moodlink.app.model.admin.book.repository.AdminBookGenreRepository;
import com.grepp.moodlink.app.model.admin.book.repository.AdminBookRepository;
import com.grepp.moodlink.app.model.admin.movie.dto.TmdbDto;
import com.grepp.moodlink.app.model.admin.movie.dto.TmdbMovieDto;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.response.ResponseCode;
import feign.template.UriUtils;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookLookupService {

    @Value("${lookup.naver.client.id}")
    private String clientId;
    @Value("${lookup.naver.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public BookDto lookup(String title) throws JsonProcessingException {

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // param 설정
        String encodedTitle = UriUtils.encode(title, StandardCharsets.UTF_8);

        String baseUrl = "https://openapi.naver.com/v1/search/book.json";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .queryParam("query",title)
            .queryParam("sort","sim")
            .build(false).toUriString();

        ResponseEntity<String> response = restTemplate.exchange(
            requestUrl,
            HttpMethod.GET,
            entity,
            String.class
        );

        NaverDto naverDto = objectMapper.readValue(response.getBody(), NaverDto.class);
        log.info("{}",naverDto.getTotal());
        NaverBookDto dto= naverDto.getItems().getFirst();

        if(dto==null){
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }
        log.info("{}",dto.getPubdate());
        return toBookDto(dto);
    }

    private BookDto toBookDto(NaverBookDto dto) {

        BookDto book = new BookDto();
        book.setTitle(dto.getTitle());
        book.setImage(String.valueOf(dto.getImage()));
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setPublishedDate(dto.getPubdate());
        book.setDescription(dto.getDescription());

        return book;
    }
}
