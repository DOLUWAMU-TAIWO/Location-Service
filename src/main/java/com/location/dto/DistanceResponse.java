package com.location.dto;

import java.util.Map;

public class DistanceResponse {
    public Map<String, TravelInfo> travelModes;

    public static class TravelInfo {
        public String distanceText;
        public long distanceValue;
        public String durationText;
        public long durationValue;
    }
}
