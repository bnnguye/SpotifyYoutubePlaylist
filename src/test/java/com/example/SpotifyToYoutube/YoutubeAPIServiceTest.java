package com.example.SpotifyToYoutube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    }

    @Test
    void parseKeyValuePairsTest() {

    }
}
