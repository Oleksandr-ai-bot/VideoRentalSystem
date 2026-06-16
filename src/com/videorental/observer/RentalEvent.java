package com.videorental.observer;

import java.time.LocalDateTime;

public class RentalEvent {
    private final EventType type;
    private final String description;
    private final String sourceId;
    private final LocalDateTime timestamp;
    private final double fineAmount;

    public RentalEvent(EventType type, String description, String sourceId) {
        this(type, description, sourceId, 0);
    }

    public RentalEvent(EventType type, String description, String sourceId, double fineAmount) {
        this.type = type;
        this.description = description;
        this.sourceId = sourceId;
        this.timestamp = LocalDateTime.now();
        this.fineAmount = fineAmount;
    }

    public EventType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getSourceId() {
        return sourceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s", timestamp, type, description);
    }
}
