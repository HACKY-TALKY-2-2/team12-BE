package com.hackytalky.team12server.dto;

import com.hackytalky.team12server.entity.Post;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RecommendedPostDto {
    private Long postId;
    private String title;
    private UserDto.Info userInfo;
    private String departureAddress;
    private String destinationAddress;
    private PostDto.TimeRange timeRange;
    private String type;
    private String transformation;

    public static RecommendedPostDto of(Post post,
                                        UserDto.Info userInfo,
                                        PostDto.TimeRange timeRange) {
        return RecommendedPostDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .userInfo(userInfo)
                .destinationAddress(post.getDestinationAddress())
                .departureAddress(post.getDepartureAddress())
                .timeRange(timeRange)
                .type(post.getType())
                .transformation(post.getTransformation())
                .build();
    }
}
