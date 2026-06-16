package com.videorental.model.copy;

import com.videorental.common.Identifiable;
import com.videorental.model.media.MediaItem;

public abstract class MediaCopy implements Identifiable {
    protected final String id;
    protected final MediaItem mediaItem;
    protected CopyStatus status;

    public MediaCopy(String id, MediaItem mediaItem) {
        this.id = id;
        this.mediaItem = mediaItem;
        this.status = CopyStatus.AVAILABLE;
    }

    @Override
    public String getId() {
        return id;
    }

    public MediaItem getMediaItem() {
        return mediaItem;
    }

    public CopyStatus getStatus() {
        return status;
    }

    public void setStatus(CopyStatus status) {
        this.status = status;
    }

    public abstract String getFormatInfo();

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %s", id, mediaItem.getTitle(), getFormatInfo(), status);
    }
}
