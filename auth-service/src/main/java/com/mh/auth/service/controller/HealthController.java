package com.mh.auth.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller for monitoring service status.
 */
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    /**
     * Basic health check endpoint.
     *
     * @return service status
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {

        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "auth-service");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}
