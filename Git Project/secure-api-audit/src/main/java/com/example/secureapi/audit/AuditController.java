package com.example.secureapi.audit;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping(value = "/audit", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> tailAudit() throws IOException {
        // Simple: return last ~200 lines
        List<String> lines = Files.readAllLines(auditService.getAuditPath());
        int from = Math.max(0, lines.size() - 200);
        return lines.subList(from, lines.size());
    }
}
