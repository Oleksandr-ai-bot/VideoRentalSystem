package com.videorental.model.media;

import com.videorental.common.Identifiable;

public abstract class MediaItem implements Identifiable {
    protected final String id;
    protected String title;
    protected String genre;
    protected int releaseYear;
    protected double rating;

    public MediaItem(String id, String title, String genre, int releaseYear, double rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public abstract String getInfo();

    @Override
    public String toString() {
        return String.format("[%s] %s (%d) | Жанр: %s | Рейтинг: %.1f", id, title, releaseYear, genre, rating);
    }
}
