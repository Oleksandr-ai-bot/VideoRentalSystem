package com.videorental.repository;

import com.videorental.model.copy.CopyStatus;
import com.videorental.model.copy.MediaCopy;

import java.util.List;
import java.util.Optional;

public class CopyRepository extends InMemoryRepository<MediaCopy> {
    private static CopyRepository instance;

    private CopyRepository() {}

    public static CopyRepository getInstance() {
        if (instance == null) {
            instance = new CopyRepository();
        }
        return instance;
    }

    public Optional<MediaCopy> findAvailableCopy(String mediaId) {
        return findAll().stream()
                .filter(c -> c.getMediaItem().getId().equals(mediaId)
                        && c.getStatus() == CopyStatus.AVAILABLE)
                .findFirst();
    }

    public List<MediaCopy> findByMediaId(String mediaId) {
        return findAll().stream()
                .filter(c -> c.getMediaItem().getId().equals(mediaId))
                .toList();
    }
}
