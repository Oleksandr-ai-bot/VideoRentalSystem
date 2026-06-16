package com.videorental.model.user;

import com.videorental.model.rental.Rental;
import com.videorental.model.review.Review;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Customer extends User {
    private final List<Rental> rentals;
    private final Set<String> wishlist;
    private final List<Review> reviews;

    public Customer(String id, String name, String email) {
        super(id, name, email);
        this.rentals = new ArrayList<>();
        this.wishlist = new HashSet<>();
        this.reviews = new ArrayList<>();
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public Set<String> getWishlist() {
        return wishlist;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
    }

    public void addToWishlist(String mediaId) {
        wishlist.add(mediaId);
    }

    public void removeFromWishlist(String mediaId) {
        wishlist.remove(mediaId);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    @Override
    public UserRole getRole() {
        return UserRole.CUSTOMER;
    }
}
