package com.example.SpotifyToYoutube;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class YoutubeAPIController {

    @PostMapping("/api/spotify")
    public void parsePlaylistData(@RequestBody String request) {
        System.out.println("Request: ");
        System.out.println(request);
    }
}
