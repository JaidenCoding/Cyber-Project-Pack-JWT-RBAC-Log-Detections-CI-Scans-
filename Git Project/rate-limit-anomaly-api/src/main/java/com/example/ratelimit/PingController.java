package com.example.ratelimit;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/api/ping")
    public Map<String, Object> ping() {
        return Map.of(
                "ok", true,
                "ts", Instant.now().toString(),
                "message", "rate-limit-anomaly-api is running"
        );
    }
}
