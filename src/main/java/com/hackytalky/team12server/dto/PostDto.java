package com.hackytalky.team12server.dto;

import com.hackytalky.team12server.entity.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static class Details {
        private Long postId;
        private String title;
        private UserDto.Details userInfo;
        private Route route;
        private TimeRange timeRange;
        private String type;
        private String profileText;

        public static Details of(Post post,
                                 UserDto.Details userInfo,
                                 Route route) {
            TimeRange timeRange = TimeRange.fromPost(post);
            return Details.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .userInfo(userInfo)
                    .timeRange(timeRange)
                    .type(post.getType())
                    .profileText(post.getBody())
                    .route(route)
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static class saveRequest {
        private String title;
        private String body;
        private Long userId;
        private String departureAddress;
        private String destinationAddress;
        private TimeRange timeRange;
        private Double payRatio;
        private String type;
        private String transformation;
        private Double departLon;
        private Double departLat;
        private Double destiLon;
        private Double destiLat;

        public static LocalDateTime parse(String dateString) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm");
            return LocalDateTime.parse(dateString, formatter);
        }

        public Post toPostEntity() {
            return Post.builder()
                    .body(body)
                    .title(title)
                    .departureAddress(departureAddress)
                    .destinationAddress(destinationAddress)
                    .departLat(departLat)
                    .departLon(departLon)
                    .destiLat(destiLat)
                    .destiLon(destiLon)
                    .start(parse(timeRange.getFrom()))
                    .end(parse(timeRange.getTo()))
                    .type(type)
                    .transformation(transformation)
                    .completed(0)
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class TimeRange {
        private String from;
        private String to;

        public static TimeRange fromPost(Post post) {
            return PostDto.TimeRange.builder()
                    .from(post.getStart().toString())
                    .to(post.getEnd().toString())
                    .build();
        }
    }
}
