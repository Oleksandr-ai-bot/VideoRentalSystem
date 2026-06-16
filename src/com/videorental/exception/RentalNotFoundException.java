package com.videorental.exception;

public class RentalNotFoundException extends RuntimeException {
    public RentalNotFoundException(String id) {
        super("Оренду не знайдено: " + id);
    }
}
