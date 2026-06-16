package com.videorental.repository;

import com.videorental.model.media.MediaItem;

public class MediaRepository extends InMemoryRepository<MediaItem> {
    private static MediaRepository instance;

    private MediaRepository() {}

    public static MediaRepository getInstance() {
        if (instance == null) {
            instance = new MediaRepository();
        }
        return instance;
    }
}
