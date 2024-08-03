package com.example.artworksharingplatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DemoController {
    @GetMapping("api/auth/creator/getCreator")
    @PreAuthorize("hasRole('ROLE_CREATOR')")
    public ResponseEntity<String> CreatorEndpoint() {
        return ResponseEntity.ok("Creator Endpoint Accessed");
    }

    @GetMapping("api/auth/adu")
    public ResponseEntity<String> userEndpoint() {
        return ResponseEntity.ok("Audience Endpoint Accessed");
    }
}
