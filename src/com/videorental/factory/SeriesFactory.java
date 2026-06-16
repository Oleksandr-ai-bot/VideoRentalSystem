package com.videorental.factory;

import com.videorental.model.media.MediaItem;
import com.videorental.model.media.Series;

public class SeriesFactory implements MediaFactory {
    @Override
    public MediaItem create(String id, String title, String genre, int releaseYear, double rating,
                            String seasonsStr, String episodesStr) {
        int seasons = Integer.parseInt(seasonsStr);
        int episodes = Integer.parseInt(episodesStr);
        return new Series(id, title, genre, releaseYear, rating, seasons, episodes);
    }
}
