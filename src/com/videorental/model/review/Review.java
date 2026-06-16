package com.videorental.model.review;

import com.videorental.common.Identifiable;
import com.videorental.model.media.MediaItem;
import com.videorental.model.user.Customer;

import java.time.LocalDate;

public class Review implements Identifiable {
    private final String id;
    private final Customer customer;
    private final MediaItem mediaItem;
    private final int rating;
    private final String comment;
    private final LocalDate createdAt;

    public Review(String id, Customer customer, MediaItem mediaItem, int rating, String comment) {
        this.id = id;
        this.customer = customer;
        this.mediaItem = mediaItem;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDate.now();
    }

    @Override
    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MediaItem getMediaItem() {
        return mediaItem;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s -> %s | Рейтинг: %d/5 | %s | %s",
                id, customer.getName(), mediaItem.getTitle(), rating, comment, createdAt);
    }
}
