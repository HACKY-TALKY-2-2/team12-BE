package com.hackytalky.team12server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Coordination {
    private Double lat;
    private Double lon;
}
