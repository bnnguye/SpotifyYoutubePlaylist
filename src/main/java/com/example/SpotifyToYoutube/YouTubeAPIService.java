package com.example.SpotifyToYoutube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class YouTubeAPIService {

    String playlistName = null;

    String playlistId = null;


    String getResults(YouTube youtube, String key, String searchResult) throws IOException {
        log.info("Search result: " + searchResult);

        YouTube.Search.List request = youtube.search().list("snippet");
        request.setKey(key);
        request.setQ(searchResult + " lyrics |" + searchResult);
        request.setType("video");
        request.setMaxResults(5L);
        request.setRegionCode("AU");

        log.info("Search: " + request.getQ());

        try {

            SearchListResponse response = request.execute();
            List<SearchResult> items = response.getItems();

            // Process the search results
            int counter = 0;
            for (SearchResult item : items) {
                counter++;
                log.info(counter + ":: Title: " + item.getSnippet().getTitle() +" | Video ID: " + item.getId().getVideoId());
            }

            if (items.size() > 0 ) {
                return items.get(0).getId().getVideoId();
            }
            return "";
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    void createNewPlayListWithName(YouTube youtube) throws IOException {
        log.info("Creating playlist...");
        if (playlistName == null) {
            playlistName = "A playlist";
        }

        Playlist playlist = new Playlist();
        PlaylistSnippet snippet = new PlaylistSnippet();
        snippet.setTitle(playlistName);
        playlist.setSnippet(snippet);

        YouTube.Playlists.Insert request = youtube.playlists()
                .insert("snippet", playlist);

        log.info("Executing request...");
        Playlist response = request.execute();

        log.info("Created playlist with ID: " + response.getId());
        playlistId = response.getId();

        ResponseEntity.ok().build();
    }

    void addToPlaylist(YouTube youTube, String videoId) throws IOException {
        if (!videoId.equals("")) {
            PlaylistItem playlistItem = new PlaylistItem();
            PlaylistItemSnippet snippet = new PlaylistItemSnippet();
            snippet.setResourceId(new ResourceId().setKind("youtube#video").setVideoId(videoId));
            snippet.setPlaylistId(playlistId);
            playlistItem.setSnippet(snippet);

            // Define and execute the API request
            YouTube.PlaylistItems.Insert request = youTube.playlistItems()
                    .insert("snippet", playlistItem);
            PlaylistItem response = request.execute();
            log.info(String.valueOf(response));
        }
    }

    ArrayList<TrackArtist> parse(List<Object> request) {
        playlistName = request.get(0).toString();
        log.info("Playlist name: " + playlistName);
        request.remove(0);

        ArrayList<TrackArtist> keyValueMap = new ArrayList<>();

        for (Map<String, String> object: (List<Map<String, String>>) request.get(0)) {
            keyValueMap.add(new TrackArtist(object.get("key"), object.get("value")));
        }

        return keyValueMap;
    }
}
