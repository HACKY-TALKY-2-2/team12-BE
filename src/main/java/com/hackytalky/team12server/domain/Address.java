package com.hackytalky.team12server.domain;

import lombok.Builder;
import lombok.Data;

import java.util.StringTokenizer;

@Data
@Builder
public class Address {
    private String city;
    private String gu;
    private String dong;

    public static Address of(String addressString) {
        StringTokenizer stringTokenizer = new StringTokenizer(addressString);

        return new Address(
                stringTokenizer.nextToken(),
                stringTokenizer.nextToken(),
                stringTokenizer.nextToken()
        );
    }
}

