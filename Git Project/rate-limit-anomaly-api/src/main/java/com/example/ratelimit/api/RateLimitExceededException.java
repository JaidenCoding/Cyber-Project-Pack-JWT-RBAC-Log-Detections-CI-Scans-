package com.example.ratelimit.api;

public class RateLimitExceededException extends RuntimeException {

    private final String path;
    private final int retryAfterSeconds;

    public RateLimitExceededException(String message, String path, int retryAfterSeconds) {
        super(message);
        this.path = path;
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public String getPath() {
        return path;
    }

    public int getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
