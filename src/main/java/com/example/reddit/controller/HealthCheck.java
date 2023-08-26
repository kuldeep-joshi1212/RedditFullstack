package com.example.reddit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/ping")
    ResponseEntity<String> ping() {
        return new ResponseEntity<>("PONG", HttpStatus.OK);
    }
}
