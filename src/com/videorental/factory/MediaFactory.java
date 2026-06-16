package com.videorental.factory;

import com.videorental.model.media.MediaItem;

public interface MediaFactory {
    MediaItem create(String id, String title, String genre, int releaseYear, double rating,
                     String param1, String param2);
}
