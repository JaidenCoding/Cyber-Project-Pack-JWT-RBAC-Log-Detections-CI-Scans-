package com.example.loganalyzer;

import com.example.loganalyzer.model.AuthEvent;
import com.example.loganalyzer.parser.AuthLogParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthLogParserTest {

    @Test
    void parsesValidLine() {
        String line = "2025-12-15T14:02:31Z user=jordan ip=10.0.0.10 event=LOGIN_FAIL device=laptop";
        AuthEvent ev = new AuthLogParser().parseLine(line);
        assertEquals("jordan", ev.user());
        assertEquals("10.0.0.10", ev.ip());
        assertEquals(AuthEvent.EventType.LOGIN_FAIL, ev.event());
        assertEquals("laptop", ev.device());
    }
}
