package com.example.SpotifyToYoutube;

import com.google.api.client.json.JsonFactory;
import com.google.api.services.youtube.YouTube;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp.Browser;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp.FlowDefinition;
import com.google.api.client.extensions.java6.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
public class YoutubeAPIController {

    @Value("${youtube.key")
    private String key;

    @PostMapping("/api/spotify")
    public ResponseEntity<Void> process(@RequestBody List<Object> request) {
        System.out.println("Request: ");
        System.out.println(request);

        String result = getResults(searchBuilder(request.get(0)));

        System.out.println(result);

        return ResponseEntity.ok().build();
    }

    public String getResults(String searchResult) throws GeneralSecurityException, IOException {

        YouTube youtube = new YouTube.Builder(
                com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport(),
                JsonFactory.getDefaultInstance(),
                null
        )
                .setApplicationName("YourAppName")
                .build();

        try {

            URL url = new URL("https://www.googleapis.com/youtube/v3/search");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("part", searchResult);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String searchBuilder(Object track) {
        return "";
    }

}
