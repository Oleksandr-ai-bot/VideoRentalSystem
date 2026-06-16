package com.videorental.exception;

public class MediaNotFoundException extends RuntimeException {
    public MediaNotFoundException(String id) {
        super("Медіа не знайдено: " + id);
    }
}
