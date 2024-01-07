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
public class YoutubeAPIServiceTest {

    @Autowired
    YoutubeAPIService service;

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
        item1.put("key", "My Favourite Muse");
        item1.put("value", "ZUHAIR");

        Map<String, String> item2 = new HashMap<>();
        item2.put("key", "Indigo");
        item2.put("value", "88rising");

        Map<String, String> item3 = new HashMap<>();
        item3.put("key", "The Weekend");
        item3.put("value", "88rising");

        playlistItems.add(item1);
        playlistItems.add(item2);
        playlistItems.add(item3);

        request.add(playlistItems);

        System.out.println("Test" + request);

        assertEquals(playlistItems, service.parse(request));
    }
}
