package com.hackytalky.team12server.dto;

import lombok.*;

@Data
@Builder
public class Route {
    private Double startX;
    private Double startY;
    private Double endX;
    private Double endY;
    private Double stopOverX;
    private Double stopOverY;
    private Integer travelTime;
    private Integer price;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static class postRequest {
        private Double departureLat;
        private Double departureLon;
        private Double destinationLat;
        private Double destinationLon;
    }
}
