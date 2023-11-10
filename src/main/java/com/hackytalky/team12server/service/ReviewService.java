package com.hackytalky.team12server.service;

import com.hackytalky.team12server.dto.ReviewDto;
import com.hackytalky.team12server.entity.Review;
import com.hackytalky.team12server.entity.ReviewFeedback;
import com.hackytalky.team12server.entity.User;
import com.hackytalky.team12server.repository.ReviewFeedbackRepository;
import com.hackytalky.team12server.repository.ReviewRepository;
import com.hackytalky.team12server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewFeedbackRepository reviewFeedbackRepository;
    private final UserRepository userRepository;

    public Long saveReview(ReviewDto.saveRequest saveRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow();
        Review review = saveRequest.toReviewEntity();
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);
        saveRequest.getComments().forEach((feedback) -> {
            reviewFeedbackRepository.save(ReviewFeedback.builder()
                    .feedback(feedback)
                    .review(savedReview)
                    .build());
        });
        return savedReview.getReviewId();
    }
}
