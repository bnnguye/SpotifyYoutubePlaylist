package com.example.SpotifyToYoutube;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Import (WebConfig.class)
public class Spotify2YoutubeApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		SpringApplicationBuilder builder = new SpringApplicationBuilder(Spotify2YoutubeApplication.class);

		builder.headless(false);

		ConfigurableApplicationContext context = builder.run(args);
	}


}
