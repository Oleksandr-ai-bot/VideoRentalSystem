package com.videorental.model.copy;

import com.videorental.model.media.MediaItem;

public class DigitalCopy extends MediaCopy {
    private final int fileSizeMb;

    public DigitalCopy(String id, MediaItem mediaItem, int fileSizeMb) {
        super(id, mediaItem);
        this.fileSizeMb = fileSizeMb;
    }

    public int getFileSizeMb() {
        return fileSizeMb;
    }

    @Override
    public String getFormatInfo() {
        return "Цифровий формат: " + fileSizeMb + " MB";
    }
}
