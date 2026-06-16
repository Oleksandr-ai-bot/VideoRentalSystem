package com.videorental.model.copy;

import com.videorental.model.media.MediaItem;

public class PhysicalCopy extends MediaCopy {
    private final Carrier carrier;

    public PhysicalCopy(String id, MediaItem mediaItem, Carrier carrier) {
        super(id, mediaItem);
        this.carrier = carrier;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    @Override
    public String getFormatInfo() {
        return "Фізичний носій: " + carrier;
    }
}
