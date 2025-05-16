package com.grepp.moodlink.infra.imgbb;

import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.response.ResponseCode;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class ImgUploadTemplate {

    @Value("${upload.imgbb.api-key}")
    private String apiKey;

    @Value("${upload.imgbb.url}")
    private String url;

    private WebClient webClient;

    @PostConstruct
    private void initWebClient() {
        this.webClient = WebClient.builder()
            .baseUrl(url)
            .build();
    }

    public String uploadImage(MultipartFile file, String name) throws IOException {
        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

        String response =  webClient.post()
            .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters
                .fromFormData("image", base64Image)
                .with("name", name))
            .retrieve()
            .bodyToMono(ImgbbUploadResponse.class)
            .map(ImgbbUploadResponse::getDisplayUrl)
            .onErrorMap(WebClientResponseException.GatewayTimeout.class, ex ->
                new CommonException(ResponseCode.INTERNAL_SERVER_ERROR))
            .block();

        return response;
    }
}
