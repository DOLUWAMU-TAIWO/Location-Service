package com.location.controller;

import com.location.service.GoogleGeocodingService;
import com.location.service.GoogleGeocodingService.GeoPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/geocode")
public class GeocodeController {

    private final GoogleGeocodingService geocodingService;

    public GeocodeController(GoogleGeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping
    public ResponseEntity<?> geocode(@RequestParam String address) {
        return geocodingService.geocode(address)
                .map(loc -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("lat", loc.lat());
                    response.put("lng", loc.lng());
                    response.put("formattedAddress", loc.formattedAddress());
                    response.put("locationType", loc.locationType());
                    response.put("partialMatch", loc.partialMatch());
                    response.put("placeId", loc.placeId());
                    response.put("streetNumber", loc.streetNumber());
                    response.put("street", loc.street());
                    response.put("city", loc.city());
                    response.put("state", loc.state());
                    response.put("country", loc.country());
                    response.put("postalCode", loc.postalCode());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "No result found")));
    }
    @GetMapping("/reverse")
    public ResponseEntity<?> reverseGeocode(@RequestParam double lat, @RequestParam double lng) {
        return geocodingService.reverseGeocode(lat, lng)
                .map(loc -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("lat", loc.lat());
                    response.put("lng", loc.lng());
                    response.put("formattedAddress", loc.formattedAddress());
                    response.put("locationType", loc.locationType());
                    response.put("partialMatch", loc.partialMatch());
                    response.put("placeId", loc.placeId());
                    response.put("streetNumber", loc.streetNumber());
                    response.put("street", loc.street());
                    response.put("city", loc.city());
                    response.put("state", loc.state());
                    response.put("country", loc.country());
                    response.put("postalCode", loc.postalCode());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "No result found")));
    }
    @GetMapping("/distance")
    public Map<String, Object> distance(
            @RequestParam double lat1, @RequestParam double lng1,
            @RequestParam double lat2, @RequestParam double lng2
    ) {
        double dist = geocodingService.calculateDistanceKm(lat1, lng1, lat2, lng2);
        return Map.of(
                "distanceKm", dist,
                "roundedKm", Math.round(dist * 10.0) / 10.0
        );
    }
    @GetMapping("/placeid")
    public ResponseEntity<?> geocodeByPlaceId(@RequestParam String placeId) {
        return geocodingService.geocodeByPlaceId(placeId)
                .map(loc -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("lat", loc.lat());
                    response.put("lng", loc.lng());
                    response.put("formattedAddress", loc.formattedAddress());
                    response.put("locationType", loc.locationType());
                    response.put("partialMatch", loc.partialMatch());
                    response.put("placeId", loc.placeId());
                    response.put("streetNumber", loc.streetNumber());
                    response.put("street", loc.street());
                    response.put("city", loc.city());
                    response.put("state", loc.state());
                    response.put("country", loc.country());
                    response.put("postalCode", loc.postalCode());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "No result found")));
    }


}
