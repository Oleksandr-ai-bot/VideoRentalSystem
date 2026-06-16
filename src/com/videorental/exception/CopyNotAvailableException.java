package com.videorental.exception;

public class CopyNotAvailableException extends RuntimeException {
    public CopyNotAvailableException(String mediaId) {
        super("Немає доступних копій для медіа: " + mediaId);
    }
}
