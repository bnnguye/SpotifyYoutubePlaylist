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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.*;

@Controller
@Slf4j
public class YoutubeAPIController {

    @Value("${youtube.key}")
    private String key;

    @Autowired
    YoutubeAPIService service;

    private static final String APP_NAME = "playlist-generator";

    private static final String CLIENT_SECRETS_FILE = "/youtube-client.json";

    private static final Collection<String> SCOPES =
            Collections.singletonList("https://www.googleapis.com/auth/youtube.force-ssl");

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static YouTube youtube = null;

    @PostMapping("/api/spotify")
    public ResponseEntity<Void> process(@RequestBody List<Object> request) throws GeneralSecurityException, IOException {
        Map<String, String> keyValueMap = service.parse(request);
        List<String> videoIds = new ArrayList<>();
        int counter = 0;

        log.info("Total tracks to be added: " + keyValueMap.entrySet().size());
        getService();
        for (Map.Entry<String, String> track: keyValueMap.entrySet()) {
            String result = service.getResults(youtube, key, track.getKey() + " " + track.getValue());
            videoIds.add(result);

            counter++;
            log.info(counter + " End result: " + result);
        }

        service.createNewPlayListWithName(youtube);
        service.compilePlaylist(youtube, videoIds);

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

        String REDIRECT_URI = "http://localhost:8081/Callback";

        String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();

//        openLink(authorizationUrl);


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

    public static void openLink(String URL) {
        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            //specify the protocol along with the URL
            URI oURL = new URI(
                    URL);
            desktop.browse(oURL);
        } catch (URISyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
