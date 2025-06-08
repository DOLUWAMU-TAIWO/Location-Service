package com.location.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class GeocodeResponse {
    public List<Result> results;
    public String status;
    public PlusCode plus_code; // Optional, present in some responses

    public static class PlusCode {
        public String global_code;
        public String compound_code;

        @Override
        public String toString() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                return super.toString();
            }
        }
    }

    public static class Result {
        public List<AddressComponent> address_components;
        public Geometry geometry;
        public String formatted_address;
        public String place_id;
        public boolean partial_match;
        public String[] types;
        public PlusCode plus_code; // Optional, present in some results

        @Override
        public String toString() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                return super.toString();
            }
        }

        public static class AddressComponent {
            public String long_name;
            public String short_name;
            public List<String> types;

            @Override
            public String toString() {
                try {
                    return new ObjectMapper().writeValueAsString(this);
                } catch (JsonProcessingException e) {
                    return super.toString();
                }
            }
        }

        public static class Geometry {
            public Location location;
            public String location_type;
            public Viewport viewport;

            @Override
            public String toString() {
                try {
                    return new ObjectMapper().writeValueAsString(this);
                } catch (JsonProcessingException e) {
                    return super.toString();
                }
            }

            public static class Location {
                public double lat;
                public double lng;

                @Override
                public String toString() {
                    try {
                        return new ObjectMapper().writeValueAsString(this);
                    } catch (JsonProcessingException e) {
                        return super.toString();
                    }
                }
            }

            public static class Viewport {
                public Location northeast;
                public Location southwest;

                @Override
                public String toString() {
                    try {
                        return new ObjectMapper().writeValueAsString(this);
                    } catch (JsonProcessingException e) {
                        return super.toString();
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}