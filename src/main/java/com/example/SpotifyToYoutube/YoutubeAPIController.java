package com.example.SpotifyToYoutube;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;

@Controller
@Slf4j
public class YoutubeAPIController {

    @Value("${youtube.key}")
    private String key;

    private static final String APP_NAME = "playlist-generator";

    private static final String CLIENT_SECRETS_FILE = "/youtube-client.json";

    private static final Collection<String> SCOPES =
            Collections.singletonList("https://www.googleapis.com/auth/youtube.force-ssl");

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static YouTube youtube = null;

    private static String playlistName = null;

    private static String playlistId = null;

    @PostMapping("/api/spotify")
    public ResponseEntity<Void> process(@RequestBody List<Object> request) throws GeneralSecurityException, IOException {

        getService();

        HashMap<String, String> dict = parse(request);

        List<String> videoIds = new ArrayList<>();

        int counter = 0;

        System.out.println("Total tracks to be added: " + dict.entrySet().size());

        for (Map.Entry<String, String> track: dict.entrySet()) {
            String result = getResults(track.getKey() + " " + track.getValue());

            videoIds.add(result);

            counter++;
            log.info(counter + " End result: " + result);
        }

        createNewPlayListWithName(null);
        compilePlaylist(videoIds);

        return ResponseEntity.ok().build();
    }

    private String getResults(String searchResult) throws IOException {
        log.info("Search result: " + searchResult);

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
                log.info("Video ID: " + item.getId().getVideoId());
                log.info("Title: " + item.getSnippet().getTitle());
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

    private ResponseEntity<Void> createNewPlayListWithName(String name) throws IOException {
        log.info("Creating playlist...");
        if (name == null) {
            name = "A playlist";
        }

        Playlist playlist = new Playlist();
        PlaylistSnippet snippet = new PlaylistSnippet();
        snippet.setTitle(name);
        playlist.setSnippet(snippet);

        YouTube.Playlists.Insert request = youtube.playlists()
                .insert("snippet", playlist);

        System.out.println("Executing request...");
        Playlist response = request.execute();

        System.out.println("Playlist ID: " + response.getId());
        playlistId = response.getId();

        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Void> compilePlaylist(List<String> videoIds) throws IOException {
        for (String videoId: videoIds) {
            if (!videoId.equals("")) {
                PlaylistItem playlistItem = new PlaylistItem();
                PlaylistItemSnippet snippet = new PlaylistItemSnippet();
                snippet.setResourceId(new ResourceId().setKind("youtube#video").setVideoId(videoId));
                snippet.setPlaylistId(playlistId);
                playlistItem.setSnippet(snippet);

                // Define and execute the API request
                YouTube.PlaylistItems.Insert request = youtube.playlistItems()
                        .insert("snippet", playlistItem);
                PlaylistItem response = request.execute();
                System.out.println(response);
            }
        }
        return ResponseEntity.ok().build();
    }

    public static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        InputStream in = Spotify2YoutubeApplication.class.getResourceAsStream(CLIENT_SECRETS_FILE);
        assert in != null;
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .build();

        LocalServerReceiver localServerReceiver = new LocalServerReceiver.Builder().setPort(8081).build();
        Credential credential =
                new AuthorizationCodeInstalledApp(flow, localServerReceiver).authorize("user");
        return credential;
    }

    public static void getService() throws GeneralSecurityException, IOException {
        System.out.println("Getting service");
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        System.out.println("httpTransport created");
        Credential credential = authorize(httpTransport);
        System.out.println("Credentials created");
        youtube = new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APP_NAME)
                .build();
        System.out.println("Service completed");
    }

}
