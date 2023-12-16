package com.example.SpotifyToYoutube;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String, String> getBearerToken() {
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
                String extractedToken = tokenExtractor.extractToken(response.body());

                // Return a JSON object
                Map<String, String> tokenResponse = new HashMap<>();
                tokenResponse.put("access_token", extractedToken);
                tokenResponse.put("token_type", "Bearer");
                tokenResponse.put("expires_in", "3600");

                return tokenResponse;
            } else {
                // Handle error
                System.err.println("Error: " + response.statusCode() + " - " + response.body());
                return Collections.emptyMap();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("api/test/{resource}")
    public String getString(@PathVariable String resource) {
        return "This is a string: " + resource;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/main")
    public String mainPage(Model model) {
        System.out.println("Loading main page");
        model.addAttribute("message", "Welcome to Thymeleaf!");
        return "index";
    }

    @GetMapping("api/{spotifyId}")
    public String convertPage(@PathVariable String spotifyId) {

        String api = "https://api.spotify.com/v1/playlists/";
        api += spotifyId;
        System.out.println("Calling api request: " + api);
        return "convert";
    }
}
