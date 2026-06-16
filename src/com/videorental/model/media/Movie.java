package com.videorental.model.media;

public class Movie extends MediaItem {
    private final String director;
    private final int durationMinutes;

    public Movie(String id, String title, String genre, int releaseYear, double rating,
                 String director, int durationMinutes) {
        super(id, title, genre, releaseYear, rating);
        this.director = director;
        this.durationMinutes = durationMinutes;
    }

    public String getDirector() {
        return director;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public String getInfo() {
        return String.format("Фільм: %s | Режисер: %s | Тривалість: %d хв", title, director, durationMinutes);
    }
}
