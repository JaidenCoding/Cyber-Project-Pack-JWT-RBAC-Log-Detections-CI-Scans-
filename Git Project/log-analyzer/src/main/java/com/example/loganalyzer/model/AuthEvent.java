package com.example.loganalyzer.model;

import java.time.Instant;

public record AuthEvent(
        Instant ts,
        String user,
        String ip,
        EventType event,
        String device
) {
    public enum EventType { LOGIN_FAIL, LOGIN_SUCCESS }
}
