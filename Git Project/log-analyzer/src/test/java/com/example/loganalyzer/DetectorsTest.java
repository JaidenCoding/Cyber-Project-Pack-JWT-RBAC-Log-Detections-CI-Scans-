package com.example.loganalyzer;

import com.example.loganalyzer.detect.Detectors;
import com.example.loganalyzer.model.AuthEvent;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static com.example.loganalyzer.model.AuthEvent.EventType.LOGIN_FAIL;
import static org.junit.jupiter.api.Assertions.*;

class DetectorsTest {

    @Test
    void bruteForceTriggersAtThreshold() {
        Instant t = Instant.parse("2025-12-15T14:00:00Z");
        List<AuthEvent> events = List.of(
                new AuthEvent(t.plusSeconds(0), "u", "1.2.3.4", LOGIN_FAIL, "x"),
                new AuthEvent(t.plusSeconds(10), "u", "1.2.3.4", LOGIN_FAIL, "x"),
                new AuthEvent(t.plusSeconds(20), "u", "1.2.3.4", LOGIN_FAIL, "x"),
                new AuthEvent(t.plusSeconds(30), "u", "1.2.3.4", LOGIN_FAIL, "x"),
                new AuthEvent(t.plusSeconds(40), "u", "1.2.3.4", LOGIN_FAIL, "x")
        );

        var alerts = new Detectors().detectBruteForce(events, Duration.ofMinutes(10), 5);
        assertFalse(alerts.isEmpty());
        assertEquals("BRUTE_FORCE", alerts.get(0).alertType());
    }
}
