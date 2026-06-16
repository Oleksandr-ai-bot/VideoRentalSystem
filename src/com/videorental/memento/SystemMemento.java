package com.videorental.memento;

import com.videorental.model.copy.MediaCopy;
import com.videorental.model.media.MediaItem;
import com.videorental.model.rental.Rental;
import com.videorental.model.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SystemMemento {
    private final List<MediaItem> media;
    private final List<MediaCopy> copies;
    private final List<User> users;
    private final List<Rental> rentals;
    private final LocalDateTime savedAt;

    public SystemMemento(List<MediaItem> media, List<MediaCopy> copies,
                         List<User> users, List<Rental> rentals) {
        this.media = new ArrayList<>(media);
        this.copies = new ArrayList<>(copies);
        this.users = new ArrayList<>(users);
        this.rentals = new ArrayList<>(rentals);
        this.savedAt = LocalDateTime.now();
    }

    public List<MediaItem> getMedia() {
        return media;
    }

    public List<MediaCopy> getCopies() {
        return copies;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    @Override
    public String toString() {
        return String.format("Знімок стану від %s | Медіа: %d | Копії: %d | Користувачі: %d | Оренди: %d",
                savedAt, media.size(), copies.size(), users.size(), rentals.size());
    }
}
