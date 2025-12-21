package com.example.loganalyzer.detect;

import java.time.Instant;

public record Alert(
        String alertType,
        String user,
        String ip,
        Instant firstSeen,
        Instant lastSeen,
        int count,
        String details
) { }
