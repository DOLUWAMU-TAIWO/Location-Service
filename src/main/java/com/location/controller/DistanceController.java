package com.location.controller;

import com.location.dto.DistanceRequest;
import com.location.dto.DistanceResponse;
import com.location.service.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DistanceController {

    @Autowired
    private DistanceService distanceService;

    @PostMapping("/distance")
    public ResponseEntity<DistanceResponse> getDistances(@RequestBody DistanceRequest request) {
        if (request.origin == null || request.destination == null
                || request.origin.isBlank() || request.destination.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        DistanceResponse response = distanceService.calculateDistances(request.origin, request.destination);
        return ResponseEntity.ok(response);
    }
}
