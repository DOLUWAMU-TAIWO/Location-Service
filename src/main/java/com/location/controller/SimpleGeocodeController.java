package com.location.controller;

import com.location.service.GoogleGeocodingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simple-geocode")
public class SimpleGeocodeController {
    private final GoogleGeocodingService geocodingService;

    public SimpleGeocodeController(GoogleGeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping
    public ResponseEntity<?> simpleGeocode(@RequestParam String address) {
        // Call the simple method in the service
        return ResponseEntity.ok(geocodingService.simpleGeocode(address));
    }
}

