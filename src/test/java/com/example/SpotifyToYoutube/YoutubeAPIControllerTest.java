package com.example.SpotifyToYoutube;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Spotify2YoutubeApplication.class)
public class YoutubeAPIControllerTest {

    @Autowired
    YoutubeAPIController youtubeAPIController;

    @Test
    public void authorizeTest() {

    }

    @Test
    public void getServiceTest() {

    }

    @Test
    public void parseKeyValuePairsTest() {
        String data = "{key=Artist, value=Title}";
        Map<String, String> output = new HashMap<>();
        output.put("key", "Artist");
        output.put("value", "Title");

        assertEquals(youtubeAPIController.service.parseKeyValuePairs(data), output);
    }
}
