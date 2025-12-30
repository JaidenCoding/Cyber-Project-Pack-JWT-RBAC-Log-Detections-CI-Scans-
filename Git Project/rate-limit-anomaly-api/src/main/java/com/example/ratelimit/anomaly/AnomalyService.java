package com.example.ratelimit.anomaly;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnomalyService {

    // simple detection rules:
    // 1) brute force: N failures within window minutes (per user)
    // 2) new IP for user: first time user seen from an IP

    private final Map<String, Deque<Instant>> userFailTimes = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> userKnownIps = new ConcurrentHashMap<>();

    private final int threshold = 5;
    private final Duration window = Duration.ofMinutes(10);

    public boolean process(String user, String ip, String event) {
        boolean flagged = false;

        // new IP for user
        Set<String> ips = userKnownIps.computeIfAbsent(user, k -> ConcurrentHashMap.newKeySet());
        if (ips.add(ip)) {
            // first time seeing this ip for this user
            flagged = true;
        }

        // brute force threshold (only for LOGIN_FAIL)
        if ("LOGIN_FAIL".equalsIgnoreCase(event)) {
            Deque<Instant> times = userFailTimes.computeIfAbsent(user, k -> new ArrayDeque<>());
            Instant now = Instant.now();
            times.addLast(now);

            // evict old
            while (!times.isEmpty() && times.peekFirst().isBefore(now.minus(window))) {
                times.removeFirst();
            }

            if (times.size() >= threshold) {
                flagged = true;
            }
        }

        return flagged;
    }
}
