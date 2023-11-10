package com.hackytalky.team12server.dto;

import com.hackytalky.team12server.entity.Review;
import com.hackytalky.team12server.entity.ReviewFeedback;
import lombok.*;

import java.util.List;

public class ReviewDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static class saveRequest {
        private Long reviewerId;
        private Integer grade;
        private List<String> comments;

        public Review toReviewEntity() {
            return Review.builder()
                    .grade(grade)
                    .build();
        }

    }
}
