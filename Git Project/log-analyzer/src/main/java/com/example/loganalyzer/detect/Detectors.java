package com.example.loganalyzer.detect;

import com.example.loganalyzer.model.AuthEvent;
import com.example.loganalyzer.model.AuthEvent.EventType;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Detectors {

    public List<Alert> detectBruteForce(List<AuthEvent> events, Duration window, int threshold) {
        // Count LOGIN_FAIL per (user, ip) in sliding window
        List<AuthEvent> fails = events.stream()
                .filter(e -> e.event() == EventType.LOGIN_FAIL)
                .sorted(Comparator.comparing(AuthEvent::ts))
                .toList();

        Map<String, Deque<AuthEvent>> buckets = new HashMap<>();
        List<Alert> alerts = new ArrayList<>();

        for (AuthEvent e : fails) {
            String key = e.user() + "|" + e.ip();
            Deque<AuthEvent> q = buckets.computeIfAbsent(key, k -> new ArrayDeque<>());
            q.addLast(e);

            Instant cutoff = e.ts().minus(window);
            while (!q.isEmpty() && q.peekFirst().ts().isBefore(cutoff)) {
                q.removeFirst();
            }

            if (q.size() == threshold) {
                // Fire alert once when threshold hit
                AuthEvent first = q.peekFirst();
                AuthEvent last = q.peekLast();
                alerts.add(new Alert(
                        "BRUTE_FORCE",
                        e.user(),
                        e.ip(),
                        first.ts(),
                        last.ts(),
                        q.size(),
                        ">= " + threshold + " failures within " + window.toMinutes() + "m"
                ));
            }
        }

        return dedupe(alerts);
    }

    public List<Alert> detectNewIpPerUser(List<AuthEvent> events) {
        // First time we see (user, ip) -> NEW_IP
        List<AuthEvent> ordered = events.stream()
                .sorted(Comparator.comparing(AuthEvent::ts))
                .toList();

        Set<String> seen = new HashSet<>();
        List<Alert> alerts = new ArrayList<>();

        for (AuthEvent e : ordered) {
            String key = e.user() + "|" + e.ip();
            if (!seen.contains(key)) {
                seen.add(key);
                alerts.add(new Alert(
                        "NEW_IP",
                        e.user(),
                        e.ip(),
                        e.ts(),
                        e.ts(),
                        1,
                        "First time user seen from this IP in dataset"
                ));
            }
        }
        return alerts;
    }

    private List<Alert> dedupe(List<Alert> alerts) {
        // Simple dedupe: keep unique (type,user,ip,firstSeen,lastSeen)
        return alerts.stream()
                .collect(Collectors.toMap(
                        a -> a.alertType()+"|"+a.user()+"|"+a.ip()+"|"+a.firstSeen()+"|"+a.lastSeen(),
                        a -> a,
                        (a,b) -> a
                ))
                .values().stream()
                .sorted(Comparator.comparing(Alert::firstSeen))
                .toList();
    }
}
