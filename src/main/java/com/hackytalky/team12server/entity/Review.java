package com.hackytalky.team12server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Review {
    @Id
    private String reviewId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private int grade;

    @OneToMany(mappedBy = "review")
    private List<ReviewFeedback> reviewFeedbacks;
}
