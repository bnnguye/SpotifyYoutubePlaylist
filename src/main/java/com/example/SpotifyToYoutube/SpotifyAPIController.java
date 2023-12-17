package com.example.SpotifyToYoutube;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Controller
public class SpotifyAPIController {

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    private String bearerToken;

    String tokenEndpoint = "https://accounts.spotify.com/api/token";

    @Autowired
    private TokenExtractor tokenExtractor;

    @GetMapping("/api/get/bearerToken")
    @ResponseBody
    public String getBearerToken() {
        String requestBody = "grant_type=client_credentials";
        String authHeaderValue = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenEndpoint))
                .header("Authorization", "Basic " + authHeaderValue)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request and handle the response
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return tokenExtractor.extractToken(response.body());
            } else {
                // Handle error
                System.err.println("Error: " + response.statusCode() + " - " + response.body());
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("api/{spotifyId}")
    public String convertPage(@PathVariable String spotifyId) {

        String api = "https://api.spotify.com/v1/playlists/";
        api += spotifyId;
        System.out.println("Calling api request: " + api);
        return "convert";
    }
}
