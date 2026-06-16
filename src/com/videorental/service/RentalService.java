package com.videorental.service;

import com.videorental.exception.CopyNotAvailableException;
import com.videorental.exception.MediaNotFoundException;
import com.videorental.exception.RentalNotFoundException;
import com.videorental.exception.UserNotFoundException;
import com.videorental.model.copy.CopyStatus;
import com.videorental.model.copy.MediaCopy;
import com.videorental.model.media.MediaItem;
import com.videorental.model.rental.Rental;
import com.videorental.model.review.Review;
import com.videorental.model.user.Customer;
import com.videorental.model.user.User;
import com.videorental.observer.EventManager;
import com.videorental.observer.EventType;
import com.videorental.observer.RentalEvent;
import com.videorental.repository.CopyRepository;
import com.videorental.repository.MediaRepository;
import com.videorental.repository.RentalRepository;
import com.videorental.repository.UserRepository;

import java.time.LocalDate;
import java.util.UUID;

public class RentalService {
    private static final double DAILY_RATE = 25.0;

    private final MediaRepository mediaRepo;
    private final CopyRepository copyRepo;
    private final UserRepository userRepo;
    private final RentalRepository rentalRepo;
    private final EventManager eventManager;

    public RentalService(MediaRepository mediaRepo, CopyRepository copyRepo,
                         UserRepository userRepo, RentalRepository rentalRepo,
                         EventManager eventManager) {
        this.mediaRepo = mediaRepo;
        this.copyRepo = copyRepo;
        this.userRepo = userRepo;
        this.rentalRepo = rentalRepo;
        this.eventManager = eventManager;
    }

    public MediaItem addMedia(MediaItem media) {
        mediaRepo.add(media);
        eventManager.notify(new RentalEvent(EventType.MEDIA_ADDED,
                "Додано медіа: " + media.getTitle(), media.getId()));
        return media;
    }

    public MediaCopy addCopy(MediaCopy copy) {
        copyRepo.add(copy);
        eventManager.notify(new RentalEvent(EventType.COPY_ADDED,
                "Додано копію для: " + copy.getMediaItem().getTitle(),
                copy.getMediaItem().getId()));
        return copy;
    }

    public Customer registerCustomer(String id, String name, String email) {
        Customer customer = new Customer(id, name, email);
        userRepo.add(customer);
        eventManager.notify(new RentalEvent(EventType.CUSTOMER_REGISTERED,
                "Зареєстровано клієнта: " + name, id));
        return customer;
    }

    public Rental rentMedia(String customerId, String mediaId, int days) {
        User user = userRepo.findById(customerId);
        if (user == null) {
            throw new UserNotFoundException(customerId);
        }
        Customer customer = (Customer) user;

        MediaItem media = mediaRepo.findById(mediaId);
        if (media == null) {
            throw new MediaNotFoundException(mediaId);
        }

        MediaCopy copy = copyRepo.findAvailableCopy(mediaId)
                .orElseThrow(() -> new CopyNotAvailableException(mediaId));

        copy.setStatus(CopyStatus.RENTED);

        String rentalId = "R-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Rental rental = new Rental(rentalId, customer, copy, LocalDate.now(), days, DAILY_RATE);

        rentalRepo.add(rental);
        customer.addRental(rental);

        eventManager.notify(new RentalEvent(EventType.RENTAL_CREATED,
                "Клієнт " + customer.getName() + " орендував \"" + media.getTitle() + "\" на " + days + " днів",
                mediaId));

        return rental;
    }

    public void returnMedia(String rentalId) {
        Rental rental = rentalRepo.findById(rentalId);
        if (rental == null) {
            throw new RentalNotFoundException(rentalId);
        }
        if (rental.isReturned()) {
            throw new IllegalStateException("Оренда вже повернена: " + rentalId);
        }

        rental.setReturnDate(LocalDate.now());
        rental.getCopy().setStatus(CopyStatus.AVAILABLE);

        eventManager.notify(new RentalEvent(EventType.RENTAL_RETURNED,
                "Клієнт " + rental.getCustomer().getName() + " повернув \""
                        + rental.getCopy().getMediaItem().getTitle() + "\"",
                rental.getCopy().getMediaItem().getId()));

        if (rental.isOverdue()) {
            double fine = rental.calculateFine();
            eventManager.notify(new RentalEvent(EventType.FINE_CHARGED,
                    "Штраф для " + rental.getCustomer().getName() + ": " + String.format("%.2f", fine) + " грн",
                    rental.getId(), fine));
        }
    }

    public Review addReview(String customerId, String mediaId, int rating, String comment) {
        User user = userRepo.findById(customerId);
        if (user == null) {
            throw new UserNotFoundException(customerId);
        }
        Customer customer = (Customer) user;

        MediaItem media = mediaRepo.findById(mediaId);
        if (media == null) {
            throw new MediaNotFoundException(mediaId);
        }

        String reviewId = "REV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Review review = new Review(reviewId, customer, media, rating, comment);
        customer.addReview(review);

        eventManager.notify(new RentalEvent(EventType.REVIEW_ADDED,
                "Клієнт " + customer.getName() + " залишив відгук для \"" + media.getTitle()
                        + "\" (" + rating + "/5)",
                mediaId));

        return review;
    }

    public void addToWishlist(String customerId, String mediaId) {
        User user = userRepo.findById(customerId);
        if (user == null) {
            throw new UserNotFoundException(customerId);
        }
        Customer customer = (Customer) user;

        MediaItem media = mediaRepo.findById(mediaId);
        if (media == null) {
            throw new MediaNotFoundException(mediaId);
        }

        customer.addToWishlist(mediaId);
        eventManager.notify(new RentalEvent(EventType.WISHLIST_UPDATED,
                "Клієнт " + customer.getName() + " додав \"" + media.getTitle() + "\" до списку бажаного",
                mediaId));
    }

    public void removeFromWishlist(String customerId, String mediaId) {
        User user = userRepo.findById(customerId);
        if (user == null) {
            throw new UserNotFoundException(customerId);
        }
        Customer customer = (Customer) user;

        customer.removeFromWishlist(mediaId);
        eventManager.notify(new RentalEvent(EventType.WISHLIST_UPDATED,
                "Клієнт " + customer.getName() + " видалив медіа зі списку бажаного",
                mediaId));
    }
}
