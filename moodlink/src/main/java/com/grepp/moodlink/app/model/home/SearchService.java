package com.grepp.moodlink.app.model.home;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Value("${youtube.api.key}")
    private String apiKey;

    public String searchYoutubeMovieTrailer(String title) throws Exception {
        String encodedQuery = URLEncoder.encode(title + " 예고편", StandardCharsets.UTF_8);
        String urlStr = String.format(
            "https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s&type=video&maxResults=1&key=%s",
            encodedQuery, apiKey
        );

        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");

        String response = new BufferedReader(new InputStreamReader(conn.getInputStream()))
            .lines().collect(Collectors.joining());

        JSONObject json = new JSONObject(response);
        return json.getJSONArray("items")
            .getJSONObject(0)
            .getJSONObject("id")
            .getString("videoId");
    }

    public String searchYoutubeMusicTrailer(String title) throws Exception {
        String encodedQuery = URLEncoder.encode( "[MV]" + title, StandardCharsets.UTF_8);
        String urlStr = String.format(
            "https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s&type=video&maxResults=1&key=%s",
            encodedQuery, apiKey
        );

        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");

        String response = new BufferedReader(new InputStreamReader(conn.getInputStream()))
            .lines().collect(Collectors.joining());

        JSONObject json = new JSONObject(response);
        return json.getJSONArray("items")
            .getJSONObject(0)
            .getJSONObject("id")
            .getString("videoId");
    }
}