    package com.example.loganalyzer.io;

    import com.example.loganalyzer.detect.Alert;

    import java.io.BufferedWriter;
    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.StandardOpenOption;
    import java.util.List;

    public class CsvWriter {

        public void write(Path outPath, List<Alert> alerts) throws IOException {
            Files.createDirectories(outPath.getParent());

            try (BufferedWriter w = Files.newBufferedWriter(outPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                w.write("alert_type,user,ip,first_seen,last_seen,count,details");
                w.newLine();
                for (Alert a : alerts) {
                    w.write(escape(a.alertType())); w.write(",");
                    w.write(escape(a.user())); w.write(",");
                    w.write(escape(a.ip())); w.write(",");
                    w.write(escape(a.firstSeen().toString())); w.write(",");
                    w.write(escape(a.lastSeen().toString())); w.write(",");
                    w.write(Integer.toString(a.count())); w.write(",");
                    w.write(escape(a.details()));
                    w.newLine();
                }
            }
        }

        private String escape(String s) {
            if (s == null) return "";
            if (s.contains(",") || s.contains(""") || s.contains("
")) {
                return """ + s.replace(""", """") + """;
            }
            return s;
        }
    }
