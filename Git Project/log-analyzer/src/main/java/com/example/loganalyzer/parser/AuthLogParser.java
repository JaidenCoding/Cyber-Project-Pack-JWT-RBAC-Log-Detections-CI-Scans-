package com.example.loganalyzer.parser;

import com.example.loganalyzer.model.AuthEvent;
import com.example.loganalyzer.model.AuthEvent.EventType;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class AuthLogParser {

    public AuthEvent parseLine(String line) {
        // Format:
        // 2025-12-15T14:02:31Z user=jordan ip=10.0.0.10 event=LOGIN_FAIL device=laptop
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 4) throw new IllegalArgumentException("Bad line: " + line);

        Instant ts = Instant.parse(parts[0]);
        Map<String, String> kv = new HashMap<>();
        for (int i = 1; i < parts.length; i++) {
            String p = parts[i];
            int eq = p.indexOf('=');
            if (eq <= 0) continue;
            kv.put(p.substring(0, eq), p.substring(eq + 1));
        }

        String user = require(kv, "user", line);
        String ip = require(kv, "ip", line);
        String ev = require(kv, "event", line);
        String device = kv.getOrDefault("device", "unknown");

        EventType type = EventType.valueOf(ev);
        return new AuthEvent(ts, user, ip, type, device);
    }

    private String require(Map<String, String> kv, String key, String line) {
        String v = kv.get(key);
        if (v == null || v.isBlank()) throw new IllegalArgumentException("Missing " + key + " in: " + line);
        return v;
    }
}
