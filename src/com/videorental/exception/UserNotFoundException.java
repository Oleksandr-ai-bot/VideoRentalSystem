package com.videorental.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super("Користувача не знайдено: " + id);
    }
}
