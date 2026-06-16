package com.videorental.factory;

import com.videorental.model.media.MediaItem;
import com.videorental.model.media.Movie;

public class MovieFactory implements MediaFactory {
    @Override
    public MediaItem create(String id, String title, String genre, int releaseYear, double rating,
                            String director, String durationStr) {
        int duration = Integer.parseInt(durationStr);
        return new Movie(id, title, genre, releaseYear, rating, director, duration);
    }
}
