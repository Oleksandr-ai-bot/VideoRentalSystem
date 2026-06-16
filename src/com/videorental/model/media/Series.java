package com.videorental.model.media;

public class Series extends MediaItem {
    private final int seasons;
    private final int episodesPerSeason;

    public Series(String id, String title, String genre, int releaseYear, double rating,
                  int seasons, int episodesPerSeason) {
        super(id, title, genre, releaseYear, rating);
        this.seasons = seasons;
        this.episodesPerSeason = episodesPerSeason;
    }

    public int getSeasons() {
        return seasons;
    }

    public int getEpisodesPerSeason() {
        return episodesPerSeason;
    }

    @Override
    public String getInfo() {
        return String.format("Серіал: %s | Сезонів: %d | Серій/сезон: %d", title, seasons, episodesPerSeason);
    }
}
