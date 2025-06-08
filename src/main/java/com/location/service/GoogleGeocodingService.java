package com.location.service;

import com.location.dto.GeocodeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

@Service
public class GoogleGeocodingService {

    @Value("${geocoding.google.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(GoogleGeocodingService.class);

    public Optional<GeoPoint> geocode(String address) {
        logger.info("Geocoding address: {}", address);
        String encoded = UriUtils.encodeQueryParam(address, StandardCharsets.UTF_8);
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s", encoded, apiKey);
        logger.info("Request URL: {}", url);

        // Log raw JSON response
        try {
            String rawJson = restTemplate.execute(url, HttpMethod.GET, null, new ResponseExtractor<String>() {
                @Override
                public String extractData(ClientHttpResponse response) throws IOException {
                    return new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                }
            });
            logger.info("Raw JSON response: {}", rawJson);
        } catch (RestClientException e) {
            logger.error("Error fetching raw JSON response: {}", e.getMessage(), e);
        }

        ResponseEntity<GeocodeResponse> response = restTemplate.getForEntity(url, GeocodeResponse.class);
        logger.info("Raw response: {}", response);
        GeocodeResponse body = response.getBody();
        logger.info("Deserialized GeocodeResponse: {}", body);

        if (body != null && body.results != null && !body.results.isEmpty()) {
            var result = body.results.get(0);
            var loc = result.geometry.location;
            // Defensive extraction of address components
            String streetNumber = null, street = null, city = null, state = null, country = null, postalCode = null;
            if (result.address_components != null) {
                for (var comp : result.address_components) {
                    if (comp.types.contains("street_number")) streetNumber = comp.long_name;
                    else if (comp.types.contains("route")) street = comp.long_name;
                    else if (comp.types.contains("locality")) city = comp.long_name;
                    else if (comp.types.contains("administrative_area_level_1")) state = comp.long_name;
                    else if (comp.types.contains("country")) country = comp.long_name;
                    else if (comp.types.contains("postal_code")) postalCode = comp.long_name;
                }
            }
            return Optional.of(new GeoPoint(
                    loc.lat,
                    loc.lng,
                    result.formatted_address,
                    result.geometry.location_type,
                    result.partial_match,
                    result.place_id,
                    streetNumber,
                    street,
                    city,
                    state,
                    country,
                    postalCode
            ));
        }

        logger.warn("No geocoding result found for address: {}", address);
        return Optional.empty();
    }

    public Optional<GeoPoint> reverseGeocode(double lat, double lng) {
        logger.info("Reverse geocoding lat: {}, lng: {}", lat, lng);
        String url = String.format(
                "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s",
                lat, lng, apiKey
        );
        logger.info("Request URL: {}", url);

        // Log raw JSON response
        try {
            String rawJson = restTemplate.execute(url, HttpMethod.GET, null, new ResponseExtractor<String>() {
                @Override
                public String extractData(ClientHttpResponse response) throws IOException {
                    return new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                }
            });
            logger.info("Raw JSON response: {}", rawJson);
        } catch (RestClientException e) {
            logger.error("Error fetching raw JSON response: {}", e.getMessage(), e);
        }

        ResponseEntity<GeocodeResponse> response = restTemplate.getForEntity(url, GeocodeResponse.class);
        logger.info("Raw response: {}", response);
        GeocodeResponse body = response.getBody();
        logger.info("Deserialized GeocodeResponse: {}", body);

        if (body != null && body.results != null && !body.results.isEmpty()) {
            var result = body.results.get(0);
            var loc = result.geometry.location;
            // Defensive extraction of address components
            String streetNumber = null, street = null, city = null, state = null, country = null, postalCode = null;
            if (result.address_components != null) {
                for (var comp : result.address_components) {
                    if (comp.types.contains("street_number")) streetNumber = comp.long_name;
                    else if (comp.types.contains("route")) street = comp.long_name;
                    else if (comp.types.contains("locality")) city = comp.long_name;
                    else if (comp.types.contains("administrative_area_level_1")) state = comp.long_name;
                    else if (comp.types.contains("country")) country = comp.long_name;
                    else if (comp.types.contains("postal_code")) postalCode = comp.long_name;
                }
            }
            return Optional.of(new GeoPoint(
                    loc.lat,
                    loc.lng,
                    result.formatted_address,
                    result.geometry.location_type,
                    result.partial_match,
                    result.place_id,
                    streetNumber,
                    street,
                    city,
                    state,
                    country,
                    postalCode
            ));
        }

        logger.warn("No reverse geocoding result found for lat: {}, lng: {}", lat, lng);
        return Optional.empty();
    }

    public Optional<GeoPoint> geocodeByPlaceId(String placeId) {
        String encoded = UriUtils.encodeQueryParam(placeId, StandardCharsets.UTF_8);
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?place_id=%s&key=%s", encoded, apiKey);

        ResponseEntity<GeocodeResponse> response = restTemplate.getForEntity(url, GeocodeResponse.class);
        GeocodeResponse body = response.getBody();

        if (body != null && body.results != null && !body.results.isEmpty()) {
            var result = body.results.get(0);
            var loc = result.geometry.location;
            // Defensive extraction of address components
            String streetNumber = null, street = null, city = null, state = null, country = null, postalCode = null;
            if (result.address_components != null) {
                for (var comp : result.address_components) {
                    if (comp.types.contains("street_number")) streetNumber = comp.long_name;
                    else if (comp.types.contains("route")) street = comp.long_name;
                    else if (comp.types.contains("locality")) city = comp.long_name;
                    else if (comp.types.contains("administrative_area_level_1")) state = comp.long_name;
                    else if (comp.types.contains("country")) country = comp.long_name;
                    else if (comp.types.contains("postal_code")) postalCode = comp.long_name;
                }
            }
            return Optional.of(new GeoPoint(
                    loc.lat,
                    loc.lng,
                    result.formatted_address,
                    result.geometry.location_type,
                    result.partial_match,
                    result.place_id,
                    streetNumber,
                    street,
                    city,
                    state,
                    country,
                    postalCode
            ));
        }

        return Optional.empty();
    }

    public double calculateDistanceKm(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Simple geocode method for demonstration
    public Map<String, Object> simpleGeocode(String address) {
        Map<String, Object> result = new HashMap<>();
        // Use the main geocode method, but only return lat/lng or error
        Optional<GeoPoint> geo = geocode(address);
        if (geo.isPresent()) {
            result.put("lat", geo.get().lat());
            result.put("lng", geo.get().lng());
            result.put("address", address);
        } else {
            result.put("error", "No result found");
        }
        return result;
    }


    public record GeoPoint(
            double lat,
            double lng,
            String formattedAddress,
            String locationType,
            boolean partialMatch,
            String placeId,
            String streetNumber,
            String street,
            String city,
            String state,
            String country,
            String postalCode
    ) {}
}
