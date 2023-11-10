package com.hackytalky.team12server.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoutePoint {
    private String viaPointId;
    private String viaPointName;
    private String viaX;
    private String viaY;
}
