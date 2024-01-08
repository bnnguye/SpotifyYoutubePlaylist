package com.example.SpotifyToYoutube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class YouTubeAPIServiceTest {

    @Autowired
    YouTubeAPIService service;

    @Mock
    YouTube.Search.List youTubeSL;

    @Mock
    YouTube youTube;

    @Test
    void getResultsTest() throws IOException {

        SearchListResponse response = new SearchListResponse();
        Mockito.when(youTubeSL.execute()).thenReturn(response);

        assertEquals(service.getResults(youTube, "test", "searchResult"), "asd");
    }

    @Test
    void createNewPlayListWithNameTest() {

    }

    @Test
    void compilePlaylistTest() {

    }

    @Test
    void parseTest() {
        List<Object> request = new ArrayList<>();
        request.add("Test Playlist");

        List<Map<String, String>> playlistItems = new ArrayList<>();

        Map<String, String> item1 = new HashMap<>();
        item1.put("key", "ZUHAIR");
        item1.put("value", "My Favourite Muse");

        Map<String, String> item2 = new HashMap<>();
        item2.put("key", "88rising");
        item2.put("value", "Indigo");


        Map<String, String> item3 = new HashMap<>();
        item3.put("key", "88rising");
        item3.put("value", "The Weekend");

        playlistItems.add(item1);
        playlistItems.add(item2);
        playlistItems.add(item3);

        request.add(playlistItems);

        ArrayList<TrackArtist> result = new ArrayList<>();
        for (Map<String, String> item: playlistItems) {
            boolean toggle = true;
            TrackArtist trackArtist = new TrackArtist();
            for (String key: item.keySet()) {
                if (toggle) {
                    trackArtist.setTitle(item.get(key));
                }
                else {
                    trackArtist.setArtist(item.get(key));
                }
                toggle = !toggle;
            }
            result.add(trackArtist);
        }

        assertEquals(result, service.parse(request));
    }
}
