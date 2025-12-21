package com.example.loganalyzer;

import com.example.loganalyzer.detect.Alert;
import com.example.loganalyzer.detect.Detectors;
import com.example.loganalyzer.io.CsvWriter;
import com.example.loganalyzer.model.AuthEvent;
import com.example.loganalyzer.parser.AuthLogParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        Args cfg = Args.parse(args);

        Path in = Path.of(cfg.inPath);
        Path out = Path.of(cfg.outPath);

        List<String> lines = Files.readAllLines(in);
        AuthLogParser parser = new AuthLogParser();
        List<AuthEvent> events = new ArrayList<>();

        for (String line : lines) {
            if (line == null || line.isBlank() || line.startsWith("#")) continue;
            events.add(parser.parseLine(line));
        }

        Detectors detectors = new Detectors();
        List<Alert> alerts = new ArrayList<>();
        alerts.addAll(detectors.detectBruteForce(events, Duration.ofMinutes(cfg.windowMinutes), cfg.threshold));
        alerts.addAll(detectors.detectNewIpPerUser(events));

        new CsvWriter().write(out, alerts);

        System.out.println("Parsed events: " + events.size());
        System.out.println("Alerts written: " + alerts.size());
        System.out.println("Report: " + out.toAbsolutePath());
    }

    static class Args {
        String inPath;
        String outPath;
        int windowMinutes = 10;
        int threshold = 5;

        static Args parse(String[] args) {
            Args a = new Args();
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--in" -> a.inPath = args[++i];
                    case "--out" -> a.outPath = args[++i];
                    case "--windowMinutes" -> a.windowMinutes = Integer.parseInt(args[++i]);
                    case "--threshold" -> a.threshold = Integer.parseInt(args[++i]);
                    default -> { /* ignore */ }
                }
            }
            if (a.inPath == null || a.outPath == null) {
                throw new IllegalArgumentException("Usage: --in <path> --out <path> [--windowMinutes N] [--threshold N]");
            }
            return a;
        }
    }
}
