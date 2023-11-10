package com.hackytalky.team12server.repository;

import com.hackytalky.team12server.entity.ReviewFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewFeedbackRepository extends JpaRepository<ReviewFeedback, Long> {
}
