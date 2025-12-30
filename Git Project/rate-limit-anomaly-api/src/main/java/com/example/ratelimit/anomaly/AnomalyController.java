package com.example.ratelimit.anomaly;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/anomaly")
public class AnomalyController {

    private final AnomalyService service;

    public AnomalyController(AnomalyService service) {
        this.service = service;
    }

    @PostMapping("/event")
    public ResponseEntity<?> ingest(@Valid @RequestBody AuthEvent event) {
        boolean flagged = service.process(event.user(), event.ip(), event.event());
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "flagged", flagged,
                "ts", Instant.now().toString()
        ));
    }
}
