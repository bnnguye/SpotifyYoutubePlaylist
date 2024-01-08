package com.example.SpotifyToYoutube;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrackArtist {
    private String artist;
    private String title;
    private String id;

    public TrackArtist(String artist, String title) {
        this.artist = artist;
        this.title = title;
    }

    public String toString() {
        return artist + " " + title;
    }

}
