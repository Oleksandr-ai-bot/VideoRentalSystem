package com.videorental.factory;

import com.videorental.model.media.Game;
import com.videorental.model.media.MediaItem;

public class GameFactory implements MediaFactory {
    @Override
    public MediaItem create(String id, String title, String genre, int releaseYear, double rating,
                            String platform, String publisher) {
        return new Game(id, title, genre, releaseYear, rating, platform, publisher);
    }
}
