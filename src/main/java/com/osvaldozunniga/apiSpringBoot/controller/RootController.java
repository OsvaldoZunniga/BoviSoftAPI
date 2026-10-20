package com.osvaldozunniga.apiSpringBoot.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> rootPath() {
        return ResponseEntity.ok(Collections.singletonMap(
                "message",
                "Welcome to BoviSoft APIREST. Access endpoints at /api/cattle and /api/farm-events"
        ));
    }

    @GetMapping("/api")
    public ResponseEntity<Map<String, String>> apiPath() {
        return ResponseEntity.ok(Collections.singletonMap(
                "message",
                "BoviSoft API root endpoint"
        ));
    }

    @GetMapping({"/health", "/api/health"})
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Collections.singletonMap("status", "UP"));
    }
}
