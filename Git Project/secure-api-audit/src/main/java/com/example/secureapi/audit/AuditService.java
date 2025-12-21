package com.example.secureapi.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class AuditService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Path auditPath;

    public AuditService() throws IOException {
        Path logsDir = Path.of("logs");
        Files.createDirectories(logsDir);
        this.auditPath = logsDir.resolve("audit.log");
        if (!Files.exists(auditPath)) Files.createFile(auditPath);
    }

    public void append(AuditEvent event) {
        try (BufferedWriter out = Files.newBufferedWriter(
                auditPath,
                StandardOpenOption.APPEND
        )) {
            out.write(mapper.writeValueAsString(event));
            out.newLine();
        } catch (IOException ignored) {
            // For demo: ignore logging failure (avoid breaking API)
        }
    }

    public Path getAuditPath() {
        return auditPath;
    }
}
