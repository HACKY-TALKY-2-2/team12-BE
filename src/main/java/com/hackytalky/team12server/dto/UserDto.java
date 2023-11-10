package com.hackytalky.team12server.dto;

import com.hackytalky.team12server.entity.Post;
import com.hackytalky.team12server.entity.Review;
import com.hackytalky.team12server.entity.User;
import com.hackytalky.team12server.service.TaxichatService;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDto {
    //게시글 세부사항용
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static class Details {
        private Long userId;
        private String nickname;
        private String image;
        private Integer reviewCount;
        private Integer grade;
        private String company;
        private String category;
        private String job;
        private List<String> reviews;

        public static Details of(User user, List<String> reviews, Integer grade) {
            return Details.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .image(user.getImage())
                    .reviewCount(user.getReviews().size())
                    .grade(grade)
                    .company(user.getCompany())
                    .category(user.getCategory())
                    .job(user.getJob())
                    .reviews(reviews)
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static class Info {
        private Long userId;
        private String nickname;
        private String image;
        private Integer reviewCount;
        private String category;


        public static Info fromUser(User user) {
            return UserDto.Info.builder()
                    .image(user.getImage())
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .reviewCount(user.getReviews().size())
                    .category(user.getCategory())
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static class RegisterRequest {
        private String nickname;
        private String image;
        private Integer age;
        private String category;
        private String job;
        private String company;
        private String phoneNumber;
        private Integer yearOfExperience;
        private String workplaceAddress;
        private String residenceAddress;
        private Double workplaceLon;
        private Double workplaceLat;
        private Double residenceLon;
        private Double residenceLat;
        private String description;
        private List<String> personality;
        private List<String> interests;

        public User toUserEntity() {
            return User.builder()
                    .nickname(nickname)
                    .age(age)
                    .image(image)
                    .category(category)
                    .job(job)
                    .company(company)
                    .phoneNumber(phoneNumber)
                    .yearOfExperience(yearOfExperience)
                    .workplaceAddress(workplaceAddress)
                    .residenceAddress(residenceAddress)
                    .personality(personality.toString())
                    .interests(interests.toString())
                    .workplaceLat(workplaceLat)
                    .workplaceLon(workplaceLon)
                    .residenceLat(residenceLat)
                    .residenceLon(residenceLon)
                    .description(description)
                    .build();
        }
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static class TaxichatList {
        private Long userId;
        private Long postId;
        private String nickname;
        private String departureAddress;
        private String destinationAddress;
        private String type;
        private Integer completed;

        public static TaxichatList of(User user, Post post) {
            return TaxichatList.builder()
                    .userId(user.getUserId())
                    .postId(post.getPostId())
                    .nickname(user.getNickname())
                    .destinationAddress(user.getResidenceAddress())
                    .departureAddress(user.getWorkplaceAddress())
                    .type(post.getType())
                    .completed(post.getCompleted())
                    .build();
        }
    }

}
