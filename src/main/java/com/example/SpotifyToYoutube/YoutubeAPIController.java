package com.example.SpotifyToYoutube;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class YoutubeAPIController {

    @PostMapping("/api/spotify")
    public ResponseEntity<Void> parsePlaylistData(@RequestBody List<Object> request) {
        System.out.println("Request: ");
        System.out.println(request);

        return ResponseEntity.ok().build();
    }
}
