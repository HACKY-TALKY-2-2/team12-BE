package com.hackytalky.team12server.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class RouteRequest {
    private Double startX;
    private Double startY;
    private Double endX;
    private Double endY;
}
