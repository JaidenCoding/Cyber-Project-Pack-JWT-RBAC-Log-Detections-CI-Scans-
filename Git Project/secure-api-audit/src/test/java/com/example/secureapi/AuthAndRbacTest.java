package com.example.secureapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthAndRbacTest {

    @Autowired MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private String loginAndGetToken(String username, String password) throws Exception {
        String body = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
        String resp = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode node = mapper.readTree(resp);
        return node.get("token").asText();
    }

    @Test
    void apiRequiresAuth() throws Exception {
        mvc.perform(get("/api/hello"))
                .andExpect(status().isForbidden()); // no auth -> denied
    }

    @Test
    void userCanAccessApiButNotAdmin() throws Exception {
        String token = loginAndGetToken("user", "password");

        mvc.perform(get("/api/hello").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mvc.perform(get("/admin/audit").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessAdmin() throws Exception {
        String token = loginAndGetToken("admin", "password");
        mvc.perform(get("/admin/audit").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
