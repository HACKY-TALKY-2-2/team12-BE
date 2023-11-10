package com.hackytalky.team12server.service;

import com.hackytalky.team12server.domain.Address;
import com.hackytalky.team12server.domain.Coordination;
import com.hackytalky.team12server.dto.Route;
import com.hackytalky.team12server.dto.RoutePoint;
import com.hackytalky.team12server.dto.RouteRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@Service
@Slf4j
public class RouteService {
    public static final String TOTAL_TIME = "totalTime";
    public static final String PRICE = "price";
    @Value("${tmap.app-key}")
    private String appKey;
    private WebClient webClient = WebClient.builder()
            .baseUrl("https://apis.openapi.sk.com/tmap")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("appKey", appKey)
            .build();

    public Route findRoute(Coordination start, Coordination viewer, Coordination poster) {
        Map<String, Integer> viewerFirst = calculateTimeSpent(start, viewer, poster);
        Map<String, Integer> posterFirst = calculateTimeSpent(start, poster, viewer);

        if (viewerFirst.get(TOTAL_TIME) > posterFirst.get(TOTAL_TIME)) {
            return Route.builder()
                    .startX(start.getLon())
                    .startY(start.getLat())
                    .stopOverX(poster.getLon())
                    .stopOverY(poster.getLat())
                    .endX(viewer.getLon())
                    .endY(viewer.getLat())
                    .travelTime(posterFirst.get(TOTAL_TIME))
                    .price(posterFirst.get(PRICE))
                    .build();
        }
        return Route.builder()
                .startX(start.getLon())
                .startY(start.getLat())
                .stopOverX(viewer.getLon())
                .stopOverY(viewer.getLat())
                .endX(poster.getLon())
                .endY(poster.getLat())
                .travelTime(viewerFirst.get(TOTAL_TIME))
                .price(viewerFirst.get(PRICE))
                .build();
    }

    private Map<String, Integer> calculateTimeSpent(Coordination start, Coordination viewer, Coordination poster) {
        RouteRequest routeRequest = makeRouteRequest(start, viewer);
        Map<String, Object> response = sendRouteAPIRequest(routeRequest);
        List<Map<String,Object>> response2 = (List<Map<String, Object>>) response.get("features");
        Map<String, Object> response3 = (Map<String, Object>) response2.get(0).get("properties");
        Map<String, Integer> result = Map.of(
                "totalTime", (int) response3.get("totalTime"),
                "price", (int) response3.get("taxiFare")
        );
        return result;
    }

    private static RouteRequest makeRouteRequest(Coordination start, Coordination end) {
        return RouteRequest.builder()
                .startX(start.getLon())
                .startY(start.getLat())
                .endX(end.getLon())
                .endY(end.getLat())
                .build();
    }

    private Map<String, Object> sendRouteAPIRequest(RouteRequest routeRequest) {
        System.out.println(routeRequest);
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/routes")
                        .queryParam("version", 1)
                        .build())
                .header("appKey", appKey)
                .bodyValue(routeRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Coordination geoCode(String addressString) throws ParseException {
        Address address = Address.of(addressString);
        String response = webClient.get()
                .uri(uriBuilder -> {
                            try {
                                return uriBuilder
                                        .path("/geo/geocoding")
                                        .queryParam("version", 1)
                                        .queryParam("city_do", encode(address.getCity()))
                                        .queryParam("gu_gun", encode(address.getGu()))
                                        .queryParam("dong", encode(address.getDong()))
                                        .queryParam("appKey", appKey)
                                        .queryParam("addressFlag", "F02")
                                        .build();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                throw new IllegalArgumentException();
                            }
                        }
                )
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.OK) {
                        return clientResponse.bodyToMono(String.class);
                    } else {
                        log.warn("{} API Error: {} {};", this, clientResponse.statusCode(), clientResponse.statusCode());
                        return Mono.empty();
                    }
                })
                .block();

        return new Coordination(findField(response, "\"newLat\":"), findField(response, "\"newLon\":"));
    }

    private static String encode(String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, StandardCharsets.UTF_8);
    }

    private static Double findField(String json, String target) {
        StringTokenizer stringTokenizer = new StringTokenizer(json);

        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();

            if (token.equals(target)) {
                String latString = stringTokenizer.nextToken();
                return Double.parseDouble(latString.substring(1, latString.length() - 2));
            }
        }

        return null;
    }

    private static String formatTime() {
        LocalDateTime currentTime = LocalDateTime.now();

        // 원하는 형식으로 포맷팅합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedTime = currentTime.format(formatter);

        return formattedTime;
    }
}
