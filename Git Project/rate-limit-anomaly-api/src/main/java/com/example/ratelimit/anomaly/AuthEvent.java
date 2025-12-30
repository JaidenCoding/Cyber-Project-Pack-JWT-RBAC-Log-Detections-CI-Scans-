package com.example.ratelimit.anomaly;

import jakarta.validation.constraints.NotBlank;

public record AuthEvent(
        @NotBlank String user,
        @NotBlank String ip,
        @NotBlank String event // LOGIN_FAIL or LOGIN_SUCCESS
) {}
