package com.example.SpotifyToYoutube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import (WebConfig.class)
public class Spotify2YoutubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(Spotify2YoutubeApplication.class, args);
	}

}
