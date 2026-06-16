package com.videorental.model.media;

public class Game extends MediaItem {
    private final String platform;
    private final String publisher;

    public Game(String id, String title, String genre, int releaseYear, double rating,
                String platform, String publisher) {
        super(id, title, genre, releaseYear, rating);
        this.platform = platform;
        this.publisher = publisher;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPublisher() {
        return publisher;
    }

    @Override
    public String getInfo() {
        return String.format("Гра: %s | Платформа: %s | Видавець: %s", title, platform, publisher);
    }
}
