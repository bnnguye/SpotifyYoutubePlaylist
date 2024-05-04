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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;

@RestController
@Slf4j
public class YouTubeAPIController {

    @Value("${youtube.key}")
    private String key;

    @Autowired
    YouTubeAPIService service;

    @Autowired
    SseController sseController;

    private static final String APP_NAME = "playlist-generator";

    private static final String CLIENT_SECRETS_FILE = "/youtube-client.json";

    private static final Collection<String> SCOPES =
            Collections.singletonList("https://www.googleapis.com/auth/youtube.force-ssl");

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static YouTube youtube = null;

    @PostMapping("/api/spotify")
    public ResponseEntity<Void> process(@RequestBody List<Object> request) throws GeneralSecurityException, IOException {
        ArrayList<TrackArtist> keyValueMap = service.parse(request);

        log.info("Total tracks to be added: " + keyValueMap.size());
        getService();
        for (TrackArtist trackArtist: keyValueMap) {
            trackArtist.setId(service.getResults(youtube, key, trackArtist.getArtist() + " " + trackArtist.getTitle()));
        }

        service.createNewPlayListWithName(youtube);

        for (TrackArtist trackArtist: keyValueMap) {
            sseController.updateClient(trackArtist.toString());
            service.addToPlaylist(youtube, trackArtist.getId());
        }
        sseController.updateClient("Finished converting!");

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
        AuthorizationCodeInstalledApp app = new AuthorizationCodeInstalledApp(flow, localServerReceiver);

        return app.authorize("user");
    }

    public static void getService() throws GeneralSecurityException, IOException {
        log.info("Getting service");
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        log.info("httpTransport created");
        Credential credential = authorize(httpTransport);
        log.info("Credentials created");
        youtube = new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APP_NAME)
                .build();
        log.info("Service completed");
    }
}
