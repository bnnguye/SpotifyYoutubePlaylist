package com.example.SpotifyToYoutube;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SpotifyAPIController {

    @Value("${client.id}")
    private String clientID;

    @Value("${client.secret}")
    private String clientSecret;

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
