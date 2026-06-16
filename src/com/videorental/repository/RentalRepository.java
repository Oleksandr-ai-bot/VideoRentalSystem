package com.videorental.repository;

import com.videorental.model.rental.Rental;

import java.util.List;

public class RentalRepository extends InMemoryRepository<Rental> {
    private static RentalRepository instance;

    private RentalRepository() {}

    public static RentalRepository getInstance() {
        if (instance == null) {
            instance = new RentalRepository();
        }
        return instance;
    }

    public List<Rental> findActive() {
        return findAll().stream()
                .filter(r -> !r.isReturned())
                .toList();
    }

    public List<Rental> findByCustomerId(String customerId) {
        return findAll().stream()
                .filter(r -> r.getCustomer().getId().equals(customerId))
                .toList();
    }
}
