package com.example.secureapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/hello")
    public Map<String, Object> hello(Principal principal) {
        return Map.of(
                "message", "Hello from a JWT-protected endpoint.",
                "user", principal != null ? principal.getName() : null
        );
    }
}
