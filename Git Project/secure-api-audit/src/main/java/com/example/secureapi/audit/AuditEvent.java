package com.example.secureapi.audit;

import java.util.List;

public record AuditEvent(
        String ts,
        String user,
        List<String> roles,
        String method,
        String path,
        String ip,
        int status
) { }
