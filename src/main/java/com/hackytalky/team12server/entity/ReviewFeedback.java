package com.hackytalky.team12server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReviewFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "reviewId", nullable = false)
    private Review review;

    @Column(nullable = false)
    private String feedback;

    public static List<ReviewFeedback> listOf(List<String> feedbacks) {
        return feedbacks.stream()
                .map(ReviewFeedback::of)
                .toList();
    }

    public static ReviewFeedback of(String feedback) {
        return ReviewFeedback.builder()
                .feedback(feedback)
                .build();
    }
}