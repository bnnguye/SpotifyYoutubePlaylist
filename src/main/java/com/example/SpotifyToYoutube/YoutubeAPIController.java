package com.example.SpotifyToYoutube;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.google.common.base.Splitter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class YoutubeAPIController {

    @Value("${youtube.key}")
    private String key;

    private static final String CLIENT_SECRETS_FILE = "/application.json";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @PostMapping("/api/spotify")
    public ResponseEntity<Void> process(@RequestBody List<Object> request) throws GeneralSecurityException, IOException {
        System.out.println("Request: ");
        System.out.println(request);

        HashMap<String, String> dict = parse(request);

        List<String> videoIds = new ArrayList<>();

        for (Map.Entry<String, String> track: dict.entrySet()) {
            String result = getResults(track.getKey() + " " + track.getValue());

            videoIds.add(result);

            System.out.println("End result: " + result);
        }



        return ResponseEntity.ok().build();
    }

    private String getResults(String searchResult) throws GeneralSecurityException, IOException {
        System.out.println("Search result: " + searchResult);

        YouTube youtube = new YouTube.Builder(
                com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                null
        )
                .setApplicationName("playlist-generator")
                .build();

        YouTube.Search.List request = youtube.search().list("snippet");
        request.setKey(key);
        request.setQ(searchResult + "lyric audio");
        request.setType("video");
        request.setMaxResults(1L);


        try {

            SearchListResponse response = request.execute();
            List<SearchResult> items = response.getItems();

            // Process the search results
            for (SearchResult item : items) {
                System.out.println("Video ID: " + item.getId().getVideoId());
                System.out.println("Title: " + item.getSnippet().getTitle());
            }

            return items.get(0).getId().getVideoId();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private HashMap<String, String> parse(List<Object> data) {
        HashMap<String, String> dictionary = new HashMap<>();

        for (Object object: data) {
            String strObject = object.toString();
            String artist = strObject.split(",")[1].strip().replace("value=", "").replace("}", "");
            String title = strObject.split(",")[0].replace("{key=", "");
            dictionary.put(artist, title);
        }

        return dictionary;
    }

    private ResponseEntity<Void> createNewPlayListWithName(String name) {
        if (name == null) {

        }

        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Void> compilePlaylist(List<String> videoIds) {
        return ResponseEntity.ok().build();
    }

}
