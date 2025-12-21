# Auth Log Analyzer (Java CLI)

A Java command-line tool that parses authentication logs and produces:
- Brute-force / password spray alerts (N failures within a time window)
- "New IP for user" alerts (first time a user is seen from an IP in the dataset)
- A CSV report + console summary

This is designed to be a lightweight "detection engineering" project without needing Linux or Splunk.

## Quick start

**Prereqs:** Java 17+, Maven 3.9+

```bash
cd log-analyzer
mvn test
mvn -q exec:java -Dexec.args="--in sample-data/auth.log --out out/report.csv --windowMinutes 10 --threshold 5"
```

Output:
- `out/report.csv`
- console summary

## Log format

One event per line:

```
2025-12-15T14:02:31Z user=jordan ip=10.0.0.10 event=LOGIN_FAIL device=laptop
2025-12-15T14:05:11Z user=jordan ip=10.0.0.10 event=LOGIN_SUCCESS device=laptop
```

Supported events:
- `LOGIN_FAIL`
- `LOGIN_SUCCESS`

## Tips to extend (optional)
- Add per-IP thresholding to better model password sprays
- Add geo distance / "impossible travel" once you bring in a geo-IP table
- Add JSON output for ingestion into SIEM later
