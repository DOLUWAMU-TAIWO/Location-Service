package com.location.service;

import com.location.dto.DistanceRequest;
import com.location.dto.DistanceResponse;
import com.location.dto.DistanceResponse.TravelInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class DistanceService {

    @Value("${distance.matrix.google.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DistanceResponse calculateDistances(String origin, String destination) {
        Map<String, TravelInfo> modesMap = new HashMap<>();
        String[] modes = {"driving", "walking", "bicycling"};
        for (String mode : modes) {
            try {
                String url = String.format(
                        "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&mode=%s&key=%s",
                        URLEncoder.encode(origin, StandardCharsets.UTF_8),
                        URLEncoder.encode(destination, StandardCharsets.UTF_8),
                        mode,
                        apiKey
                );
                String json = restTemplate.getForObject(url, String.class);
                JsonNode root = objectMapper.readTree(json);
                JsonNode element = root.path("rows").get(0).path("elements").get(0);
                TravelInfo info = new TravelInfo();
                if ("OK".equals(element.path("status").asText())) {
                    JsonNode dist = element.path("distance");
                    JsonNode dur = element.path("duration");
                    info.distanceText = dist.path("text").asText();
                    info.distanceValue = dist.path("value").asLong();
                    info.durationText = dur.path("text").asText();
                    info.durationValue = dur.path("value").asLong();
                } else {
                    info.distanceText = null;
                    info.distanceValue = 0;
                    info.durationText = null;
                    info.durationValue = 0;
                }
                modesMap.put(mode, info);
            } catch (RestClientException | java.io.IOException e) {
                TravelInfo info = new TravelInfo();
                info.distanceText = null;
                info.distanceValue = 0;
                info.durationText = null;
                info.durationValue = 0;
                modesMap.put(mode, info);
            }
        }
        DistanceResponse response = new DistanceResponse();
        response.travelModes = modesMap;
        return response;
    }
}
