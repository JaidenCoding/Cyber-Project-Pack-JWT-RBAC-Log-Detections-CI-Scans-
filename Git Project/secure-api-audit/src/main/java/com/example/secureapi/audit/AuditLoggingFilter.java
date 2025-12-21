package com.example.secureapi.audit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditLoggingFilter extends OncePerRequestFilter {

    private final AuditService auditService;

    public AuditLoggingFilter(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        filterChain.doFilter(request, response);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getAuthorities() != null) {
            // Avoid logging login endpoint (noise), but log everything else
            String path = request.getRequestURI();
            if (path != null && path.startsWith("/auth/")) return;

            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String ip = request.getRemoteAddr();
            int status = response.getStatus();

            AuditEvent event = new AuditEvent(
                    Instant.now().toString(),
                    auth.getName(),
                    roles,
                    request.getMethod(),
                    path,
                    ip,
                    status
            );
            auditService.append(event);
        }
    }
}
