package com.example.ratelimit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RateLimitAndAnomalyTest {

    @Autowired
    private MockMvc mvc;

    private static final String TEST_IP = "10.0.0.10";

    @Test
    void pingIsRateLimited() throws Exception {
        // First 5 requests should pass (same IP)
        for (int i = 0; i < 5; i++) {
            mvc.perform(get("/api/ping")
                    .with(req -> {
                        req.setRemoteAddr(TEST_IP);
                        return req;
                    }))
                    .andExpect(status().isOk());
        }

        // 6th request should be blocked (same IP again!)
        mvc.perform(get("/api/ping")
                .with(req -> {
                    req.setRemoteAddr(TEST_IP);
                    return req;
                }))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void anomalyFlagsBruteforce() throws Exception {
        // anomaly endpoints should NOT be rate limited (your filter bypasses /api/anomaly/*)
        mvc.perform(post("/api/anomaly/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user\":\"jordan\",\"ip\":\"10.0.0.10\",\"event\":\"LOGIN_FAIL\"}")
                .with(req -> {
                    req.setRemoteAddr(TEST_IP);
                    return req;
                }))
                .andExpect(status().isOk());
    }
}
